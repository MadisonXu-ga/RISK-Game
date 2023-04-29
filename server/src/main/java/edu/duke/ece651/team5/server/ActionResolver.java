package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.team5.shared.*;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.rulechecker.AllianceChecker;

public class ActionResolver {
    public void tryResolveAllMoveOrders(HashMap<Player, Action> playerActions, Game game) {
        ArrayList<MoveOrder> allMoveOrders = new ArrayList<>();
        for (Action action : playerActions.values()) {
            allMoveOrders.addAll(action.getMoveOrders());
        }

        for (MoveOrder moveOrder : allMoveOrders) {
            String tempgetPlayerColor = moveOrder.getPlayer().getName();
            moveOrder.execute(game.getMap(), game.getPlayerByName(tempgetPlayerColor));
        }
    }

    public void tryResolveAllAttackOrders(HashMap<Player, Action> playerActions, Game game) {
        CombatResolver combatResolver = new CombatResolver();
        ArrayList<AttackOrder> allAttackOrders = new ArrayList<>();

        // execute first
        for (Action action : playerActions.values()) {
            // System.out.println(action.getAttackOrders());
            for (AttackOrder attackOrder : action.getAttackOrders()) {
                // System.out.println(attackOrder);
                String tempgetPlayerColor = attackOrder.getPlayer().getName();
                attackOrder.execute(game.getMap(), game.getPlayerByName(tempgetPlayerColor));
            }
        }

        // System.out.println("all red player attack orders: " + playerActions.get(new
        // Player("Red")).getAttackOrders());
        // merge attackorders for each player first, then all them to allAttackOrders
        for (Map.Entry<Player, Action> entry : playerActions.entrySet()) {
            List<AttackOrder> playerMergeOrders = combatResolver
                    .mergeOrderByTerriForOnePlayer(entry.getValue().getAttackOrders());
            // System.out.println("player " + entry.getKey() + " attack order after merge: "
            // + playerMergeOrders);
            allAttackOrders.addAll(playerMergeOrders);
        }
        // System.out.println(allAttackOrders);

        System.out.println("----------------try resolve all attack orders------------------");
        // System.out.println("allAttackOrders size: " + allAttackOrders.size());
        // System.out.println("-------------------------------------------------------------------");

        // then combat
        Map<String, List<AttackOrder>> attackOrderByTerris = combatResolver.mergeOrderByTerritory(allAttackOrders);
        combatResolver.resolveAttackOrder(attackOrderByTerris, game);
    }

    public void tryResolveAllResearchOrder(HashMap<Player, Action> playerActions, Game game) {
        System.out.println("tryResolveAllResearchOrder");
        for (Action action : playerActions.values()) {
            System.out.println("Iterate ready to resolve research: ");
            if (action.getResearchOrder() != null) {
                System.out.println("Research order is not null!!");
                String tempgetPlayerColor = action.getResearchOrder().getPlayer().getName();
                action.getResearchOrder().execute(game.getMap(), game.getPlayerByName(tempgetPlayerColor));
                System.out.println("Have already executed research order!!");
            }
        }
    }

    public void tryResolveAllUpgradeOrder(HashMap<Player, Action> playerActions, Game game) {
        ArrayList<UpgradeOrder> allUpgradeOrders = new ArrayList<>();
        for (Action action : playerActions.values()) {
            allUpgradeOrders.addAll(action.getUpgradeOrders());
        }

        for (UpgradeOrder upgradeOrder : allUpgradeOrders) {
            String tempgetPlayerColor = upgradeOrder.getPlayer().getName();
            upgradeOrder.execute(game.getMap(), game.getPlayerByName(tempgetPlayerColor));
        }
    }

    public void tryResolveAllAllianceOrder(HashMap<Player, Action> playerActions, Game game) {
        // make an allicance checker
        AllianceChecker allianceChecker = new AllianceChecker();
        // get allianceOrders of all players
        List<AllianceOrder> allianceOrders = new ArrayList<>();
        for (Action action : playerActions.values()) {
            if (action.getAllianceOrder() != null) {
                allianceOrders.add(action.getAllianceOrder());
            }
        }
        // 1. check if alliance exists
        Set<Set<Player>> allianceRes = allianceChecker.checkAlliance(allianceOrders);
        if (!allianceRes.isEmpty()) {
            for (Set<Player> players : allianceRes) {
                // get all(2) players' names from player set
                ArrayList<String> playerNames = new ArrayList<>();
                for (Player player : players) {
                    playerNames.add(player.getName());
                }
                // add alliance
                Player player1 = game.getPlayerByName(playerNames.get(0));
                Player player2 = game.getPlayerByName(playerNames.get(1));
                player1.addAliance(player2);
                player2.addAliance(player1);
            }
        }
        // 2. check breakup
        List<AttackOrder> allAttackOrders = new ArrayList<>();
        for (Map.Entry<Player, Action> entry : playerActions.entrySet()) {
            allAttackOrders.addAll(entry.getValue().getAttackOrders());
        }

        // TODO: checkBreak may have a bug: player get from order is different from map!
        Set<Player> breakUpRes = allianceChecker.checkBreak(allAttackOrders, game.getMap());
        System.out.println("breakUpRes: " + breakUpRes);
        if (!breakUpRes.isEmpty()) {
            for (Player player : breakUpRes) {
                String name = player.getName();
                Player playerInGame = game.getPlayerByName(name);
                game.removeBreakUpAlliance(playerInGame);
            }
        }
    }
}
