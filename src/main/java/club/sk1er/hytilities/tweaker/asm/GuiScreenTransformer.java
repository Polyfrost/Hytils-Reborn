package club.sk1er.hytilities.tweaker.asm;

import club.sk1er.hytilities.tweaker.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class GuiScreenTransformer implements HytilitiesTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.GuiScreen"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);
            if ((methodName.equals("sendChatMessage") || methodName.equals("func_175281_b")) && method.desc.equals("(Ljava/lang/String;Z)V")) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == Opcodes.INVOKEVIRTUAL && node.getNext().getNext().getOpcode() == Opcodes.RETURN && node.getNext().getNext().getNext() instanceof LabelNode) {
                        String invokeName = mapMethodNameFromNode(node);
                        if (invokeName.equals("executeCommand") || invokeName.equals("func_71556_a")) {
                            method.instructions.insert(node.getNext().getNext().getNext(), cancelMessage());
                        }
                    }
                }
            }
        }
    }

    private InsnList cancelMessage() {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "club/sk1er/hytilities/Hytilities", "INSTANCE", "Lclub/sk1er/hytilities/Hytilities;"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "club/sk1er/hytilities/Hytilities", "getChatHandler", "()Lclub/sk1er/hytilities/handlers/chat/ChatHandler;", false));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "club/sk1er/hytilities/handlers/chat/ChatHandler", "shouldSendMessage", "(Ljava/lang/String;)Z", false));
        list.add(new JumpInsnNode(Opcodes.IFNE, label));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(label);
        return list;
    }
}
