package fr.ortaria.commandes;

import fr.ortaria.dao.PlayerFactory;
import fr.ortaria.models.Guild;
import fr.ortaria.models.Player_Class;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.ortaria.dao.GuildFactory;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.Optional;

public class GuildCommandExecutor implements CommandExecutor {
    private final GuildFactory guildFactory;

    public GuildCommandExecutor() throws SQLException {
        this.guildFactory = GuildFactory.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use guild commands.");
            return true;
        }
        sender.sendMessage("Guild command received.");

        if (cmd.getName().equalsIgnoreCase("guilde")) {
            if (args.length == 0) {
                // Show help messages or guild menu
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "create":
                    try {
                        return handleCreateGuild(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "remove":
                    try {
                        return handleRemoveGuild(player);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "tag":
                    try {
                        return handleChangeTag(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "setgrade":
                    try {
                        return handleChangeRank(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "invite":
                    try {
                        return handleInvite(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "join":
                    try {
                        return handleJoinGuild(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "leave":
                    try {
                        return handleLeaveGuild(player);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "kick":
                    try {
                        return handleKick(player, args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "gchat":
                    try {
                        return handleGChat(player);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                case "menu":
                    // Implement GUI menu for guild
                    return true;
                default:
                    sender.sendMessage("Unknown guild command.");
                    return true;
            }
        }
        return false;
    }

    private boolean handleJoinGuild(Player player, String[] args) throws SQLException {
        if (args.length < 1) {
            player.sendMessage("Usage: /guilde join <playerName>");
            return true;
        }

        String invitingPlayerName = args[1];
        if (player.getName().equalsIgnoreCase(invitingPlayerName)) {
            player.sendMessage("Vous ne pouvez pas rejoindre votre propre invitation.");
            return true;
        }

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optJoiningPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        if (!optJoiningPlayer.isPresent()) {
            player.sendMessage("Erreur: Impossible de récupérer vos informations de joueur.");
            return true;
        }

        Player_Class joiningPlayer = optJoiningPlayer.get();
        Player invitingPlayer = Bukkit.getPlayerExact(invitingPlayerName);
        if (invitingPlayer == null) {
            System.out.println("Inviting player name: " + invitingPlayerName);
            player.sendMessage("Le joueur qui vous a invité n'est pas en ligne ou n'existe pas.");
            return true;
        }

        Optional<Player_Class> optInvitingPlayer = playerFactory.getPlayer(invitingPlayer.getUniqueId().toString());
        if (!optInvitingPlayer.isPresent()) {
            player.sendMessage("Impossible de trouver les informations du joueur qui a envoyé l'invitation.");
            return true;
        }

        Player_Class invitingPlayerClass = optInvitingPlayer.get();
        if (joiningPlayer.getInvitation_guilde() != invitingPlayerClass.getId_guilde()) {
            player.sendMessage("Vous n'avez pas d'invitation valide de ce joueur pour rejoindre sa guilde.");
            return true;
        }

        // Récupération de la guilde depuis la cache
        GuildFactory guildFactory = GuildFactory.getInstance();
        Optional<Guild> optGuild = guildFactory.getGuild(invitingPlayerClass.getId_guilde());
        if (!optGuild.isPresent()) {
            player.sendMessage("Guilde non trouvée.");
            return true;
        }

        Guild guild = optGuild.get();

        // Ajouter le joueur à la liste des membres de la guilde en cache
        guild.getMembres().add(joiningPlayer.getUuid());

        // Joindre la guilde
        joiningPlayer.setId_guilde(invitingPlayerClass.getId_guilde());
        joiningPlayer.setRank_guilde(4);  // Nouveau membre commence au rang 4
        joiningPlayer.setInvitation_guilde(0);  // Réinitialiser l'invitation

        try {
            playerFactory.updatePlayer(joiningPlayer.getUuid());
            guildFactory.updateGuild(guild);  // Mettre à jour la guilde dans la base de données et la cache
            player.sendMessage("Vous avez rejoint la guilde avec succès.");
            return true;
        } catch (SQLException e) {
            player.sendMessage("Erreur lors de la tentative de rejoindre la guilde: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
    
    private boolean handleCreateGuild(Player player, String[] args) throws SQLException {
        // Vérifier si tous les arguments nécessaires sont présents
        if (args.length < 3) {
            player.sendMessage("§cUsage: /guilde create <nom> <tag>");
            return true;
        }

        String guildName = args[1];
        String guildTag = args[2];

        if (guildTag.length() > 4) {
            player.sendMessage("§cLe tag de la guilde ne peut pas dépasser 4 caractères.");
            return true;
        }

        // Utilisation de PlayerFactory pour obtenir les détails du joueur
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (optPlayer.isPresent()) {
            Player_Class gamePlayer = optPlayer.get();

            // Vérifier si le joueur est déjà dans une guilde
            if (gamePlayer.getId_guilde() != 0) {
                player.sendMessage("§cVous êtes déjà membre d'une guilde!");
                return true;
            }
            if (gamePlayer.getArgent() < 50000) {
                player.sendMessage("§cVous devez avoir au moins 50 000 de monnaie pour crée une guilde.");
                return true;
            }
        } else {
            player.sendMessage("§cErreur : Impossible de récupérer vos informations de joueur.");
            return true;
        }

        // Vérification de l'unicité du nom et du tag de la guilde
        GuildFactory guildFactory = GuildFactory.getInstance();
        if (guildFactory.guildNameExists(guildName) || guildFactory.guildTagExists(guildTag)) {
            player.sendMessage("§cUne guilde avec ce nom ou tag existe déjà!");
            return true;
        }

        // Créer la guilde
        try {
            List<String> members = new ArrayList<>();
            members.add(player.getUniqueId().toString()); // Ajouter le créateur comme premier membre
            Player_Class gamePlayer = optPlayer.get();
            Guild newGuild = guildFactory.createGuild(guildName, guildTag, "AUCUN", 1, 0, 0.0F, "AUCUN", members,gamePlayer);
            gamePlayer.setArgent(gamePlayer.getArgent() - 50000);
            player.sendMessage("§aGuilde créée avec succès: " + guildName);
            return true;
        } catch (Exception e) {
            player.sendMessage("§cErreur lors de la création de la guilde: " + e.getMessage());
            return true;
        }  
    }


    private boolean handleRemoveGuild(Player player) throws SQLException {
        // Utilisation de PlayerFactory pour obtenir les détails du joueur
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optPlayer.isPresent()) {
            player.sendMessage("§cErreur : Impossible de récupérer vos informations de joueur.");
            return true;
        }

        Player_Class gamePlayer = optPlayer.get();

        // Vérifier si le joueur est chef de la guilde
        if (gamePlayer.getRank_guilde() != 1) {
            player.sendMessage("§cVous devez être le chef de la guilde pour effectuer cette action.");
            return true;
        }

        // Suppression de la guilde
        try {
            GuildFactory guildFactory = GuildFactory.getInstance();
            guildFactory.removeGuild(gamePlayer.getId_guilde());
            player.sendMessage("§aLa guilde a été supprimée avec succès.");
            return true;
        } catch (SQLException e) {
            player.sendMessage("§cErreur lors de la suppression de la guilde: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }


    private boolean handleChangeTag(Player player, String[] args) throws SQLException {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /guilde tag <newTag>");
            return true;
        }

        String newTag = args[1];
        if (newTag.length() > 4) {
            player.sendMessage("§cLe tag de la guilde ne peut pas dépasser 4 caractères.");
            return true;
        }

        // Utilisation de PlayerFactory pour obtenir les détails du joueur
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optPlayer.isPresent()) {
            player.sendMessage("§cErreur : Impossible de récupérer vos informations de joueur.");
            return true;
        }

        Player_Class gamePlayer = optPlayer.get();

        if (gamePlayer.getId_guilde() == 0) {
            player.sendMessage("§cVous n'êtes membre d'aucune guilde.");
            return true;
        }

        if (!(gamePlayer.getRank_guilde() == 1 || gamePlayer.getRank_guilde() == 2)) {
            player.sendMessage("§cVous devez être chef ou sous-chef pour changer le tag de la guilde.");
            return true;
        }

        if (gamePlayer.getArgent() < 10000) {
            player.sendMessage("§cVous devez avoir au moins 10 000 de monnaie pour changer le tag de la guilde.");
            return true;
        }

        // Obtention de la guilde du joueur
        GuildFactory guildFactory = GuildFactory.getInstance();
        Optional<Guild> guildOptional = guildFactory.getGuild(gamePlayer.getId_guilde());
        if (!guildOptional.isPresent()) {
            player.sendMessage("§cErreur : Guilde non trouvée.");
            return true;
        }

        Guild guild = guildOptional.get();

        // Vérification de l'unicité du nouveau tag
        if (guildFactory.guildTagExists(newTag)) {
            player.sendMessage("§cCe tag de guilde est déjà utilisé.");
            return true;
        }

        // Mise à jour du tag de la guilde
        guild.setSuffixe(newTag);
        try {
            guildFactory.updateGuild(guild);
            gamePlayer.setArgent(gamePlayer.getArgent() - 10000);
            player.sendMessage("§aLe tag de la guilde a été mis à jour avec succès.");
            return true;
        } catch (SQLException e) {
            player.sendMessage("§cErreur lors de la mise à jour du tag de la guilde: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    private boolean handleChangeRank(CommandSender sender, String[] args) throws SQLException {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seulement les joueurs peuvent exécuter cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Usage: /guilde changerank <nomDuJoueur> <nouveauRang>");
            return true;
        }

        String targetPlayerName = args[2];
        int newRank;

        try {
            newRank = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Le rang doit être un nombre valide (2, 3 ou 4).");
            return true;
        }

        if (newRank < 2 || newRank > 4) {
            player.sendMessage("Le rang spécifié est invalide. Les rangs valides sont 2, 3, et 4.");
            return true;
        }

        if (player.getName().equalsIgnoreCase(targetPlayerName)) {
            player.sendMessage("Vous ne pouvez pas changer votre propre rang.");
            return true;
        }

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);

        if (targetPlayer == null) {
            player.sendMessage("Le joueur ciblé n'est pas en ligne ou n'existe pas.");
            return true;
        }

        Optional<Player_Class> optTargetPlayer = playerFactory.getPlayer(targetPlayer.getUniqueId().toString());

        if (!optPlayer.isPresent() || !optTargetPlayer.isPresent()) {
            player.sendMessage("Impossible de trouver les informations du joueur.");
            return true;
        }

        Player_Class currentPlayer = optPlayer.get();
        Player_Class targetPlayerClass = optTargetPlayer.get();

        if (!currentPlayer.getGuilde().equals(targetPlayerClass.getGuilde())) {
            player.sendMessage("Vous et le joueur ciblé devez être dans la même guilde pour changer de rang.");
            return true;
        }

        if ((currentPlayer.getRank_guilde() == 1 && (newRank == 2 || newRank == 3 || newRank == 4)) || (currentPlayer.getRank_guilde() == 2 && (newRank == 3 || newRank == 4))) {
            targetPlayerClass.setRank_guilde(newRank);
            try {
                playerFactory.updatePlayer(targetPlayer.getUniqueId().toString());
                player.sendMessage("Le rang de " + targetPlayer.getName() + " a été mis à jour à " + newRank + ".");
                return true;
            } catch (SQLException e) {
                player.sendMessage("Erreur lors de la mise à jour du rang: " + e.getMessage());
                return true;
            }
        } else {
            player.sendMessage("Vous n'avez pas les permissions nécessaires pour modifier ce rang.");
            return true;
        }
    }

    private boolean handleInvite(Player player, String[] args) throws SQLException {
        if (args.length < 1) {
            player.sendMessage("Usage: /guilde invite <playerName>");
            return true;
        }

        String targetPlayerName = args[1];
        System.out.println("Target player name: " + targetPlayerName);
        if (player.getName().equalsIgnoreCase(targetPlayerName)) {
            player.sendMessage("Vous ne pouvez pas vous inviter vous-même.");
            return true;
        }

        // Récupération des informations du joueur qui invite
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optInvitingPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optInvitingPlayer.isPresent()) {
            player.sendMessage("Erreur: Impossible de récupérer vos informations de joueur.");
            return true;
        }

        Player_Class invitingPlayer = optInvitingPlayer.get();

        // Vérifier que le joueur qui invite a les droits nécessaires
        if (invitingPlayer.getId_guilde() == 0 || !(invitingPlayer.getRank_guilde() == 1 || invitingPlayer.getRank_guilde() == 2)) {
            player.sendMessage("Vous devez être membre d'une guilde et avoir un rang suffisant pour inviter des joueurs.");
            return true;
        }

        // Récupération des informations du joueur cible
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage("Le joueur ciblé n'est pas en ligne ou n'existe pas.");
            return true;
        }

        Optional<Player_Class> optTargetPlayer = playerFactory.getPlayer(targetPlayer.getUniqueId().toString());
        if (!optTargetPlayer.isPresent()) {
            player.sendMessage("Impossible de trouver les informations du joueur ciblé.");
            return true;
        }

        Player_Class targetPlayerClass = optTargetPlayer.get();

        // Définir l'invitation de guilde pour le joueur ciblé
        targetPlayerClass.setInvitation_guilde(invitingPlayer.getId_guilde());
        try {
            playerFactory.updatePlayer(targetPlayerClass.getUuid());
            player.sendMessage("Vous avez invité " + targetPlayerName + " à rejoindre votre guilde.");
            targetPlayer.sendMessage("Vous avez été invité à rejoindre la guilde " + invitingPlayer.getGuilde() + ".");
            return true;
        } catch (SQLException e) {
            player.sendMessage("Erreur lors de l'invitation du joueur: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
    private boolean handleLeaveGuild(Player player) throws SQLException {
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optPlayer.isPresent()) {
            player.sendMessage("Erreur : Impossible de récupérer vos informations de joueur.");
            return true;
        }

        Player_Class gamePlayer = optPlayer.get();

        // Vérifier si le joueur est le chef de la guilde (rang 1)
        if (gamePlayer.getRank_guilde() == 1) {
            player.sendMessage("Vous êtes le chef de la guilde et ne pouvez pas la quitter. Veuillez désigner un autre chef ou dissoudre la guilde.");
            return true;
        }

        // Mise à jour de l'ID de la guilde et du rang du joueur
        try {
            GuildFactory guildFactory = GuildFactory.getInstance();
            System.out.println("Player guild id: " + gamePlayer.getId_guilde());
            guildFactory.leaveGuild(gamePlayer.getId_guilde(), gamePlayer.getUuid());
            gamePlayer.setId_guilde(0);
            gamePlayer.setRank_guilde(0);
            playerFactory.updatePlayer(gamePlayer.getUuid());
            player.sendMessage("Vous avez quitté la guilde avec succès.");
            return true;
        } catch (SQLException e) {
            player.sendMessage("Erreur lors de la tentative de quitter la guilde: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    private boolean handleKick(Player player, String[] args) throws SQLException {
        if (args.length < 1) {
            player.sendMessage("Usage: /guilde kick <playerName>");
            return true;
        }

        String targetPlayerName = args[1];
        if (player.getName().equalsIgnoreCase(targetPlayerName)) {
            player.sendMessage("Vous ne pouvez pas vous expulser vous-même de la guilde.");
            return true;
        }

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);

        if (targetPlayer == null) {
            player.sendMessage("Le joueur ciblé n'est pas en ligne ou n'existe pas.");
            return true;
        }

        Optional<Player_Class> optTargetPlayer = playerFactory.getPlayer(targetPlayer.getUniqueId().toString());
        if (!optPlayer.isPresent() || !optTargetPlayer.isPresent()) {
            player.sendMessage("Impossible de trouver les informations du joueur.");
            return true;
        }

        Player_Class currentPlayer = optPlayer.get();
        Player_Class targetPlayerClass = optTargetPlayer.get();

        if (!currentPlayer.getGuilde().equals(targetPlayerClass.getGuilde())) {
            player.sendMessage("Vous et le joueur ciblé devez être dans la même guilde.");
            return true;
        }

        if (currentPlayer.getRank_guilde() >= targetPlayerClass.getRank_guilde() || currentPlayer.getRank_guilde() > 2) {
            player.sendMessage("Vous n'avez pas l'autorisation nécessaire pour expulser ce membre de la guilde.");
            return true;
        }

        // Effectuer l'exclusion
        try {
            GuildFactory guildFactory = GuildFactory.getInstance();
            guildFactory.leaveGuild(targetPlayerClass.getId_guilde(), targetPlayerClass.getUuid());
            targetPlayerClass.setId_guilde(0);
            targetPlayerClass.setRank_guilde(0);
            playerFactory.updatePlayer(targetPlayerClass.getUuid());
            player.sendMessage("Le joueur " + targetPlayerName + " a été expulsé de la guilde.");
            targetPlayer.sendMessage("Vous avez été expulsé de la guilde.");
            return true;
        } catch (SQLException e) {
            player.sendMessage("Erreur lors de l'expulsion du joueur: " + e.getMessage());
            return true;
        }
    }


    private boolean handleGChat(Player player) throws SQLException {
        // Accéder à PlayerFactory pour obtenir les informations du joueur
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optPlayer.isPresent()) {
            player.sendMessage("Erreur: Impossible de récupérer vos informations de joueur.");
            return true;
        }

        // Obtenir les données du joueur et basculer le statut gChat
        Player_Class playerClass = optPlayer.get();
        playerClass.setGChat(!playerClass.getGChat());  // Bascule la valeur de gChat

        // Tenter de mettre à jour les informations du joueur dans la base de données
        try {
            playerFactory.updatePlayer(playerClass.getUuid());
            player.sendMessage("Le chat de guilde est maintenant " + (playerClass.getGChat() ? "activé" : "désactivé") + ".");
            return true;
        } catch (SQLException e) {
            player.sendMessage("Erreur lors de la mise à jour de l'état du chat de guilde: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

}
