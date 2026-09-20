package org.polyfrost.hytils.plugin

import net.fabricmc.loader.api.FabricLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class HytilsRebornMixinPlugin : IMixinConfigPlugin {
    override fun getMixins(): List<String> = buildList {
        if (FabricLoader.getInstance().isModLoaded("sodium")) {
            add("client.heightoverlay.BlockRendererMixin_HeightOverlay_Sodium")
        }

        //? if <26.1 {
        /*add("client.chat.ChatComponentMixin_TrackLineParents")
        add("client.chat.GuiMessageLineMixin_TrackParent")
        *///?}

        //? if >=1.21.11
        add("client.accessor.RenderTypeAccessor")

        //? if <1.21.8
        //add("client.hud.GuiMixin_HideHudElements")
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean = true

    override fun getRefMapperConfig(): String? = null

    override fun onLoad(mixinPackage: String?) {
    }

    override fun acceptTargets(myTargets: Set<String?>?, otherTargets: Set<String?>?) {
    }

    override fun preApply(targetClassName: String?, targetClass: ClassNode?, mixinClassName: String?, mixinInfo: IMixinInfo?) {
    }

    override fun postApply(targetClassName: String?, targetClass: ClassNode?, mixinClassName: String?, mixinInfo: IMixinInfo?) {
    }
}
