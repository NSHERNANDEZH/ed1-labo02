    package ed.lab;

    import java.util.ArrayDeque;
    import java.util.Deque;

    public class E02KthSmallest {

        public int kthSmallest(TreeNode<Integer> root, int k) {
            Deque<TreeNode<Integer>> stack = new ArrayDeque<>();
            int n = 0;

            // Llamada a la función para empujar los nodos izquierdos
            pushLeftNodes(root, stack);

            while (!stack.isEmpty()) {
                TreeNode<Integer> node = stack.pop(); // Obtiene el nodo superior de la pila
                n++;


                if (n == k) {
                    return node.value;
                }

                // Agrega los nodos del subárbol derecho a la pila
                pushLeftNodes(node.right, stack);
            }

            // Si no se encuentra el k-esimo elemento
            return -1;
        }

        // agregar los nodos izquierdos a la pila
        private void pushLeftNodes(TreeNode<Integer> root, Deque<TreeNode<Integer>> stack) {
            for (TreeNode<Integer> node = root; node != null; node = node.left) {
                stack.push(node); // Empuja el nodo actual a la pila
            }
        }
    }