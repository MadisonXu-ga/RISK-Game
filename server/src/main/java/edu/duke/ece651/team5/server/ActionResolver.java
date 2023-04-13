package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.shared.*;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.*;

public class ActionResolver {
    public void tryResolveAllMoveOrders(HashMap<Player, Action> playerActions, RISKMap map) {
        ArrayList<MoveOrder> allMoveOrders = new ArrayList<>();
        for (Action action : playerActions.values()) {
            allMoveOrders.addAll(action.getMoveOrders());
        }

        for (MoveOrder moveOrder : allMoveOrders) {
            moveOrder.execute(map);
        }
    }

    public void tryResolveAllAttackOrders(HashMap<Player, Action> playerActions, Game game) {
        CombatResolver combatResolver = new CombatResolver();
        ArrayList<AttackOrder> allAttackOrders = new ArrayList<>();

        // merge attackorders for each player first, then all them to allAttackOrders
        for (Map.Entry<Player, Action> entry : playerActions.entrySet()) {
            List<AttackOrder> playerMergeOrders = combatResolver
                    .mergeOrderByTerriForOnePlayer(entry.getValue().getAttackOrders());
            allAttackOrders.addAll(playerMergeOrders);
        }

        // execute first
        for (AttackOrder attackOrder : allAttackOrders) {
            attackOrder.execute(game.getMap());
        }

        // then combat
        Map<String, List<AttackOrder>> attackOrderByTerris = combatResolver.mergeOrderByTerritory(allAttackOrders);
        combatResolver.resolveAttackOrder(attackOrderByTerris, game);
    }

    public void tryResolveAllResearchOrder(HashMap<Player, Action> playerActions, RISKMap map) {
        System.out.println("tryResolveAllResearchOrder");
        for (Action action : playerActions.values()) {
            System.out.println("Iterate ready to resolve research: ");
            if (action.getResearchOrder() != null) {
                System.out.println("Research order is not null!!");
                action.getResearchOrder().execute(map);
                System.out.println("Have already executed research order!!");
            }
        }
    }

    public void tryResolveAllUpgradeOrder(HashMap<Player, Action> playerActions, RISKMap map) {
        ArrayList<UpgradeOrder> allUpgradeOrders = new ArrayList<>();
        for (Action action : playerActions.values()) {
            allUpgradeOrders.addAll(action.getUpgradeOrders());
        }

        for (UpgradeOrder upgradeOrder : allUpgradeOrders) {
            upgradeOrder.execute(map);
        }
    }
}
