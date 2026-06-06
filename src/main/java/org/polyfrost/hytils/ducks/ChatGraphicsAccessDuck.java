package org.polyfrost.hytils.ducks;

//? if >=1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector;

import java.util.function.UnaryOperator;

public interface ChatGraphicsAccessDuck {
    void hytils$applyParameters(UnaryOperator<ActiveTextCollector.Parameters> modifier);
}
//?}
