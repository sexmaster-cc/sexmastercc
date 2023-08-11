package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

public class ModelChanger extends Module {

    public final Property<Boolean> noSway =
            new Property<>(// this not nobob btw
                    false,
                    new String[]{"AntiSway", "RemoveSway", "NoSway"},
                    "Removes arm swaying when swinging your camera."
            );

    public final NumberProperty<Float> angleTranslate =
            new NumberProperty<>(
                    0.0f, -180.0f, 180.0f, 2.0f,
                    new String[]{"Angle", "AngleTranslate", "AngleTraductor"},
                    "Sets both hands on this angle."
            );

    public final NumberProperty<Float> offsetMain =
            new NumberProperty<>(0.0f, -5.0f, 5.0f, 0.1f,
                    new String[]{"OffsetMain", "offsetmainhand"},
                    "Offset of the offhand items."
            );

    public final NumberProperty<Float> offsetOff =
            new NumberProperty<>(0.0f, -5.0f, 5.0f, 0.1f,
                    new String[]{"OffsetOff", "offsetoffhand"},
                    "Offset of the mainhand item."
            );

    protected final NumberProperty<Float> translateX =
            new NumberProperty<>(0F, -2F, 2F, 0.05F,
                    new String[]{"TranslateX", "transx"},
                    "Translation of the X axis on both hands."
            );

    protected final NumberProperty<Float> translateY =
            new NumberProperty<>(0F, -2F, 2F, 0.05F,
                    new String[]{"TranslateY", "transy"},
                    "Translation of the Y axis on both hands."
            );

    protected final NumberProperty<Float> translateZ =
            new NumberProperty<>(0F, -2F, 2F, 0.05F,
                    new String[]{"TranslateZ", "transz"},
                    "Translation of the Z axis on both hands."
            );

    protected final NumberProperty<Float> scaleX =
            new NumberProperty<>(1F, 0F, 2F, 0.05F,
                    new String[]{"ScaleX", "scalx"},
                    "Scale of the X axis on both hands."
            );

    protected final NumberProperty<Float> scaleY =
            new NumberProperty<>(1F, 0F, 2F, 0.05F,
                    new String[]{"ScaleY", "scaly"},
                    "Scale of the Y axis on both hands."
            );

    protected final NumberProperty<Float> scaleZ =
            new NumberProperty<>(1F, 0F, 2F, 0.05F,
                    new String[]{"ScaleZ", "scalz"},
                    "Scale of the Z axis on both hands."
            );

    protected final NumberProperty<Float> rotateX =
            new NumberProperty<>(0F, 0F, 180F, 2F,
                    new String[]{"RotateX", "rotx"},
                    "Rotations of the X axis on both hands."
            );

    protected final NumberProperty<Float> rotateY =
            new NumberProperty<>(0F, 0F, 180F, 2F,
                    new String[]{"RotateY", "roty"},
                    "Rotations of the Y axis on both hands."
            );

    protected final NumberProperty<Float> rotateZ =
            new NumberProperty<>(0F, 0F, 180F, 2F,
                    new String[]{"RotateZ", "rotz"},
                    "Rotations of the Z axis on both hands."
            );

    public ModelChanger() {
        super("ViewModelChanger", new String[]{"ViewModelChanger", "viewmodel", "modelchanger"}, "Changes your viewmodel.", Category.RENDER);
        this.offerProperties(noSway, angleTranslate, offsetMain, offsetOff, translateX, translateY, translateZ, scaleX, scaleY, scaleZ, rotateX, rotateY, rotateZ);
        this.offerListeners(new ListenerRenderItemSide(this), new ListenerPostRender(this), new ListenerPreRender(this));
    }
}
