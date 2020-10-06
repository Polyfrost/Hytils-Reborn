/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.sk1er.hytilities.tweaker.asm;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import club.sk1er.hytilities.util.locraw.LocrawInformation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ListIterator;

public class LayerArmorBaseTransformer implements HytilitiesTransformer {

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

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        String methodName2 = mapMethodNameFromNode(next);
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
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/hytilities/tweaker/asm/LayerArmorBaseTransformer", "shouldRenderArmour", "(Lnet/minecraft/item/ItemStack;)Z", false));

        final LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, ifeq));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);

        return list;
    }

    @SuppressWarnings("unused")
    public static boolean shouldRenderArmour(ItemStack itemStack) {
        if (!HytilitiesConfig.hideArmour || itemStack == null) return true;

        Item item = itemStack.getItem();

        // armor piece is made of leather
        if (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
            LocrawInformation locraw = Hytilities.INSTANCE.getLocrawUtil().getLocrawInformation();

            if (locraw != null) {
                switch (locraw.getGameType()) {
                    case BED_WARS:
                        return false;
                    case ARCADE_GAMES:
                        // capture the wool
                        return !locraw.getGameMode().contains("PVP_CTW");
                    case DUELS:
                        return !locraw.getGameMode().contains("BRIDGE");
                }
            }
        }

        return true;
    }

}
