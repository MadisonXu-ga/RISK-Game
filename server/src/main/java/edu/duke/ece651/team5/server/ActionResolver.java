package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.shared.*;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;

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
        for (AttackOrder attackOrder : allAttackOrders) {
            String tempgetPlayerColor = attackOrder.getPlayer().getName();
            attackOrder.execute(game.getMap(), game.getPlayerByName(tempgetPlayerColor));
        }

        // merge attackorders for each player first, then all them to allAttackOrders
        for (Map.Entry<Player, Action> entry : playerActions.entrySet()) {
            List<AttackOrder> playerMergeOrders = combatResolver
                    .mergeOrderByTerriForOnePlayer(entry.getValue().getAttackOrders());
            allAttackOrders.addAll(playerMergeOrders);
        }



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
}
