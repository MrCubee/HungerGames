package fr.mrcubee.survivalgames.api.event;

import fr.mrcubee.survivalgames.step.Step;
import fr.mrcubee.survivalgames.step.StepManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StepChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private StepManager stepManager;
    private Step newStep;
    private int newStepIndex;

    public StepChangeEvent(StepManager stepManager, Step newStep) {
        this.stepManager = stepManager;
        this.newStep = newStep;
        this.newStepIndex = stepManager.getStepIndex(newStep);
    }

    public Step getNewStep() {
        return this.newStep;
    }

    public int getNewStepIndex() {
        return this.newStepIndex;
    }

    public StepManager getStepManager() {
        return this.stepManager;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
