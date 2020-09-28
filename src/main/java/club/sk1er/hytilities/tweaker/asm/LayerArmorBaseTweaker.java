package club.sk1er.hytilities.tweaker.asm;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.handlers.game.GameType;
import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class LayerArmorBaseTweaker implements HytilitiesTransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.entity.layers.LayerArmorBase"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);

            if (methodName.equals("renderLayer") || methodName.equals("func_177182_a")) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode) {
                        String methodName2 = mapMethodNameFromNode((MethodInsnNode) next);
                        if (methodName2.equals("getCurrentArmor") || methodName2.equals("func_177176_a")) {
                            method.instructions.insert(next.getNext(), checkRender());
                            break;
                        }
                    }

                }
            }
        }
    }

    private InsnList checkRender() {
        InsnList list = new InsnList();

        list.add(new VarInsnNode(Opcodes.ALOAD, 10));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/hytilities/tweaker/asm/LayerArmorBaseTweaker", "shouldRenderArmour", "(Lnet/minecraft/item/ItemStack;)Z", false));

        final LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, ifeq));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);

        return list;
    }

    public static boolean shouldRenderArmour(ItemStack itemStack) {
        if (!HytilitiesConfig.hideArmour || itemStack == null) return true;

        Item item = itemStack.getItem();

        if (Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation() == null) return true;
        LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();

        if (locraw.getGameType() == GameType.BED_WARS) {
            if (item == Items.leather_helmet || item == Items.leather_chestplate ||
                    item == Items.leather_leggings || item == Items.leather_boots) return false;
        }

        if (locraw.getGameType() == GameType.DUELS && locraw.getGameMode().contains("BRIDGE")) {
            return item != Items.leather_chestplate &&
                    item != Items.leather_leggings && item != Items.leather_boots;
        }

        return true;
    }

}