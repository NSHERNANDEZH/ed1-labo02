package ed.lab;

import java.util.Comparator;

public class E03AVLTree<T> {
    private class Node {
        T value;
        Node left, right;
        int height;

        public Node(T value) {
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private final Comparator<T> comparator;
    private int size = 0;

    public E03AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Obtener la altura de un nodo
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // Obtener el factor de balanceo de un nodo
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Rotación a la derecha
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Rotación a la izquierda
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public void insert(T value) {
       if (value==null) return;
       root =insert(root, value);
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        }

        if (comparator.compare(value, node.value) < 0) {
            node.left = insert(node.left, value);
        } else if (comparator.compare(value, node.value) > 0) {
            node.right = insert(node.right, value);
        } else {
            return node; // Claves duplicadas no permitidas
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Rotaciones para balancear el árbol
        if (balance > 1 && comparator.compare(value, node.left.value) < 0) {
            return rotateRight(node);
        }
        if (balance < -1 && comparator.compare(value, node.right.value) > 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && comparator.compare(value, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && comparator.compare(value, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void delete(T value) {
        if (value == null) return; // Validación de entrada
        root = delete(root, value);

    }

    private Node delete(Node node, T value) {
        if (node == null) {
            return null;
        }

        if (comparator.compare(value, node.value) < 0) {
            node.left = delete(node.left, value);
        } else if (comparator.compare(value, node.value) > 0) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    return null; // Nodo sin hijos
                } else {
                    node = temp; // Nodo con un hijo
                }
            } else {
                Node temp = getMinValueNode(node.right);
                node.value = temp.value;
                node.right = delete(node.right, temp.value);
            }
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = getBalance(node);

        // Rotaciones para balancear el árbol
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node getMinValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public T search(T value) {
        Node node = search(root, value);
        return node == null ? null : node.value;
    }

    private Node search(Node node, T value) {
        if (node == null || comparator.compare(value, node.value) == 0) {
            return node;
        }
        if (comparator.compare(value, node.value) < 0) {
            return search(node.left, value);
        }
        return search(node.right, value);
    }

    public int height() {
        return height(root);

    }

    public int size() {
        return size(root);

    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }
}
