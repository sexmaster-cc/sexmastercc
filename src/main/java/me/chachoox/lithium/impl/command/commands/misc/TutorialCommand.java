package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.impl.command.Command;
import net.minecraft.client.tutorial.TutorialSteps;

public class TutorialCommand extends Command {
    public TutorialCommand() {
        super(new String[]{"Tutorial", "tut"});
    }

    @Override
    public String execute() {
        mc.gameSettings.tutorialStep = TutorialSteps.NONE;
        mc.getTutorial().setStep(TutorialSteps.NONE);
        return "Set tutorial step to none";
    }

}
