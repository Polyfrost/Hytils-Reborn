package club.sk1er.hytilities.tweaker.asm;

import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class EntityPlayerSPTransformer implements HytilitiesTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.entity.EntityPlayerSP"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);
            if (methodName.equals("sendChatMessage") || methodName.equals("func_71165_d")) {
                method.instructions.insert(handleSentMessage());
            }
        }
    }

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
