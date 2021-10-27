package fr.mrcubee.hungergames.step;

import fr.mrcubee.hungergames.Game;

import java.util.ArrayList;
import java.util.List;

public class StepManager {

    private final Game game;
    private final List<Step> gameSteps;
    private int currentStep;

    public StepManager(Game game) {
        this.game = game;
        this.gameSteps = new ArrayList<Step>();
        this.currentStep = -1;
    }

    public boolean registerStep(Step step) {
        if (step == null || this.gameSteps.contains(step))
            return false;
        this.gameSteps.add(step);
        return true;
    }

    public boolean unRegisterStep(Step step) {
        if (step == null)
            return false;
        return this.gameSteps.remove(step);
    }

    public Step unRegisterStep(int index) {
        if (index < 0 || index >= this.gameSteps.size())
            return null;
        return this.gameSteps.remove(index);
    }

    public Step getStep(int index) {
        if (index < 0 || index >= this.gameSteps.size())
            return null;
        return this.gameSteps.get(index);
    }

    public int getStepIndex(Step step) {
        if (step == null)
            return -1;
        return this.gameSteps.indexOf(step);
    }

    public Step getCurrentStep() {
        return getStep(this.currentStep);
    }

    public Step getLastStep() {
        return getStep(this.currentStep - 1);
    }

    public Step getNextStep() {
        return getStep(this.currentStep + 1);
    }

    public void nextStep() {
        Step nextStep;
        Step step;

        if (this.currentStep >= this.gameSteps.size() || this.game.getGameStats().ordinal() <= 2)
            return;
        nextStep = getNextStep();
        step = getLastStep();
        if (step != null)
            step.remove();
        step = getCurrentStep();
        if (step != null)
            step.complete();
        if (nextStep != null)
            nextStep.start();
        this.currentStep++;
    }

    protected Game getGame() {
        return game;
    }
}
