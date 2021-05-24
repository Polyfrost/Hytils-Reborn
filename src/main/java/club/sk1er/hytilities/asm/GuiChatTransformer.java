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

package club.sk1er.hytilities.asm;

import club.sk1er.hytilities.forge.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class GuiChatTransformer implements HytilitiesTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.GuiChat"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            final String methodName = mapMethodName(classNode, method);
            if (methodName.equals("autocompletePlayerNames") || methodName.equals("func_146404_p_")) {
                final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                final LabelNode ifne = new LabelNode();
                while (iterator.hasNext()) {
                    AbstractInsnNode next = iterator.next();

                    if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW && ((TypeInsnNode) next).desc.equals("net/minecraft/util/ChatComponentText")) {
                        AbstractInsnNode previous = next;
                        for (int i = 0; i < 4; i++) {
                            next = next.getPrevious();
                        }

                        method.instructions.insertBefore(next, checkTextField(ifne));

                        for (int i = 0; i < 6; i++) {
                            previous = previous.getNext();
                        }

                        method.instructions.insert(previous.getNext(), ifne);
                    }
                }
                break;
            }
        }
    }

    private InsnList checkTextField(LabelNode ifne) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiChat", "field_146415_a", "Lnet/minecraft/client/gui/GuiTextField;"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/gui/GuiTextField", "func_146179_b", "()Ljava/lang/String;", false));
        list.add(new LdcInsnNode("/play "));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFNE, ifne));
        return list;
    }
}
