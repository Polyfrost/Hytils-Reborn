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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityPlayerSPTransformer implements HytilitiesTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.entity.EntityPlayerSP"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            final String methodName = mapMethodName(classNode, method);
            if (methodName.equals("sendChatMessage") || methodName.equals("func_71165_d")) {
                method.instructions.insert(handleSentMessage());
                break;
            }
        }
    }

    // if ((message = Hytilities.INSTANCE.getChatHandler().handleSentMessage(message)) != null)
    private InsnList handleSentMessage() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "club/sk1er/hytilities/Hytilities", "INSTANCE", "Lclub/sk1er/hytilities/Hytilities;"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "club/sk1er/hytilities/Hytilities", "getChatHandler", "()Lclub/sk1er/hytilities/handlers/chat/ChatHandler;", false));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "club/sk1er/hytilities/handlers/chat/ChatHandler", "handleSentMessage", "(Ljava/lang/String;)Ljava/lang/String;", false));
        list.add(new InsnNode(Opcodes.DUP));
        list.add(new VarInsnNode(Opcodes.ASTORE, 1));
        LabelNode label = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNONNULL, label));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(label);
        return list;
    }
}
