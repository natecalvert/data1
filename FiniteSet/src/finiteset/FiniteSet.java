package finiteset;

import java.util.*;

public class FiniteSet {

    interface BST {

        public int cardinality();

        public boolean isEmptyHuh();

        public boolean member(int elt);

        public BST add(int elt);

        public BST remove(int elt);

        public BST union(BST tree);

        public BST inter(BST tree);

        public BST diff(BST tree);

        public boolean equal(BST tree);

        public boolean subset(BST tree);
    }

    static class Empty implements BST {

        Empty() {
        }

        public int cardinality() {
            return 0;
        }

        public boolean isEmptyHuh() {
            return true;
        }

        public boolean member(int elt) {
            return false;
        }

        public BST add(int elt) {
            return new Tree(elt, new Empty(), new Empty());
        }

        public BST remove(int elt) {
            return new Empty();
        }

        public BST union(BST tree) {
            return tree;
        }

        public BST inter(BST tree) {
            return new Empty();
        }

        public BST diff(BST tree) {
            return tree;
        }

        public boolean equal(BST tree) {
            return tree.isEmptyHuh();
        }

        public boolean subset(BST tree) {
            return true;
        }
    }

    static class Tree implements BST {

        int data;
        BST left;
        BST right;

        Tree(int data, BST left, BST right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        public BST empty() {
            return new Empty();
        }

        public int cardinality() {
            return 1 + left.cardinality() + right.cardinality();
        }

        public boolean isEmptyHuh() {
            return false;
        }

        public boolean member(int elt) {
            if (data == elt) {
                return true;
            } else if (data > elt) {
                return left.member(elt);
            } else {
                return right.member(elt);
            }
        }

        public BST add(int elt) {
            if (data == elt) {
                return this;
            } else if (elt < data) {
                return new Tree(data, left.add(elt), right);
            } else {
                return new Tree(data, left, right.add(elt));
            }
        }

        public BST remove(int elt) {
            if (elt == data) {
                return left.union(right);
            } else if (elt < data) {
                return new Tree(data, left.remove(elt), right);
            } else {
                return new Tree(data, left, right.remove(elt));
            }
        }

        public BST union(BST tree) {
            return this.left.union(tree.union(this.right).add(this.data));
        }

        public BST inter(BST tree) {
            if (tree.member(data)) {
                return new Tree(data, left.inter(tree), right.inter(tree));
            } else {
                return left.inter(tree).union(right.inter(tree));
            }
        }

        public BST diff(BST tree) {
            if (tree.member(data)) {
                return left.union(right).diff(tree.remove(data));
            } else {
                return left.union(right).diff(tree);
            }
        }

        public boolean equal(BST tree) {
            return (this.subset(tree) && tree.subset(this));
        }

        public boolean subset(BST tree) {
            return (tree.member(data) && left.subset(tree) && right.subset(tree));
        }
    }

// Random BST stuff
    static Random randy = new Random();

    public static int randomInt(int min, int max) {
        return randy.nextInt((max - min) + 1) + min;
    }

    public static BST randomBSTHelper(BST temp, int length) {
        if (length == 0) {
            return temp;
        } else {
            int randyInt = randomInt(0, 100);
            if (!(temp.member(randyInt))) {
                return randomBSTHelper(temp.add(randyInt), length - 1);
            } else {
                return randomBSTHelper(temp, length);
            }
        }
    }

    public static BST randomBST(int length) {
        return randomBSTHelper(new Empty(), length);
    }

    public static void main(String[] args) {

        System.out.println("Gonna do some testing below!");
        System.out.println("");

// Very basic testing, checking that things seem to be okay. NOT EXHAUSTIVE. No fun properties, yet.
        BST MT = new Empty();
        BST t_4 = new Tree(4, MT, MT);
        BST t_5 = new Tree(5, MT, MT);
        BST t_7 = new Tree(7, MT, MT);
        BST t_8 = new Tree(8, MT, MT);
        BST t_6 = new Tree(6, t_5, t_7);

        System.out.println("Cardinality Tests:");
        System.out.println(MT.cardinality() + " should be " + 0);
        System.out.println(t_5.cardinality() + " should be " + 1);
        System.out.println(t_6.cardinality() + " should be " + 3);
        System.out.println("");

        System.out.println("isEmptyHuh Tests:");
        System.out.println(MT.isEmptyHuh() + " should be " + true);
        System.out.println(t_5.isEmptyHuh() + " should be " + false);
        System.out.println(t_6.isEmptyHuh() + " should be " + false);
        System.out.println("");

        System.out.println("member Tests:");
        System.out.println(MT.member(5) + " should be " + false);
        System.out.println(t_5.member(5) + " should be " + true);
        System.out.println(t_5.member(6) + " should be " + false);
        System.out.println(t_6.member(5) + " should be " + true);
        System.out.println(t_6.member(7) + " should be " + true);
        System.out.println(t_6.member(6) + " should be " + true);
        System.out.println(t_6.member(9) + " should be " + false);
        System.out.println("");

        System.out.println("add Tests:");
        System.out.println(MT.add(5).cardinality() + " should be " + 1);
        System.out.println(t_5.add(4).cardinality() + " should be " + 2);
        System.out.println(t_5.add(8).cardinality() + " should be " + 2);
        System.out.println(t_6.add(4).cardinality() + " should be " + 4);
        System.out.println(t_6.add(8).cardinality() + " should be " + 4);
        System.out.println("");

        System.out.println("remove Tests:");
        System.out.println(MT.remove(5).cardinality() + " should be " + 0);
        System.out.println(t_5.remove(4).cardinality() + " should be " + 1);
        System.out.println(t_5.remove(5).cardinality() + " should be " + 0);
        System.out.println(t_6.remove(4).cardinality() + " should be " + 3);
        System.out.println(t_6.remove(6).cardinality() + " should be " + 2);
        System.out.println(t_6.remove(7).cardinality() + " should be " + 2);
        System.out.println(t_6.remove(5).cardinality() + " should be " + 2);
        System.out.println("");

        System.out.println("union Tests:");
        System.out.println(MT.union(MT).isEmptyHuh() + " should be " + true);
        System.out.println(MT.union(t_4).isEmptyHuh() + " should be " + false);
        System.out.println(MT.union(t_4).cardinality() + " should be " + 1);
        System.out.println(t_5.union(t_4).cardinality() + " should be " + 2);
        System.out.println(t_5.union(t_7).cardinality() + " should be " + 2);
        System.out.println(t_5.union(t_8).cardinality() + " should be " + 2);
        System.out.println(t_5.union(t_7).union(t_8).cardinality() + " should be " + 3);
        System.out.println(t_5.union(t_8).union(t_7).cardinality() + " should be " + 3);
        System.out.println(t_7.union(t_8).union(t_5).cardinality() + " should be " + 3);
        System.out.println(t_7.union(t_5).union(t_8).cardinality() + " should be " + 3);
        System.out.println(t_8.union(t_7).union(t_5).cardinality() + " should be " + 3);
        System.out.println(t_8.union(t_5).union(t_7).cardinality() + " should be " + 3);
        System.out.println(t_6.union(t_4).cardinality() + " should be " + 4);
        System.out.println(t_6.union(t_8).cardinality() + " should be " + 4);
        System.out.println(t_6.union(t_8).union(t_4).cardinality() + " should be " + 5);
        System.out.println(t_6.union(t_4).union(t_8).cardinality() + " should be " + 5);
        System.out.println("");

        System.out.println("inter Tests:");
        System.out.println(MT.inter(MT).cardinality() + " should be " + 0);
        System.out.println(MT.inter(t_5).cardinality() + " should be " + 0);
        System.out.println(t_5.inter(t_4).cardinality() + " should be " + 0);
        System.out.println(t_5.inter(t_5).cardinality() + " should be " + 1);
        System.out.println(t_5.inter(t_6).cardinality() + " should be " + 1);
        System.out.println(t_6.inter(t_4).cardinality() + " should be " + 0);
        System.out.println(t_6.inter(t_5).cardinality() + " should be " + 1);
        System.out.println(t_6.inter(t_6).cardinality() + " should be " + 3);
        System.out.println(t_6.inter(t_7).cardinality() + " should be " + 1);
        System.out.println(t_6.inter(t_8).cardinality() + " should be " + 0);
        System.out.println("");

        System.out.println("diff Tests:");
        System.out.println(MT.diff(MT).cardinality() + " should be " + 0);
        System.out.println(MT.diff(t_5).cardinality() + " should be " + 1);
        System.out.println(t_5.diff(t_5).cardinality() + " should be " + 0);
        System.out.println(t_5.diff(t_6).cardinality() + " should be " + 2);
        System.out.println(t_5.diff(t_4).cardinality() + " should be " + 1);
        System.out.println(MT.diff(t_6).cardinality() + " should be " + 3);
        System.out.println(t_6.diff(MT).cardinality() + " should be " + 0);
        System.out.println(t_6.diff(t_5).cardinality() + " should be " + 0);
        System.out.println(t_6.diff(t_7).cardinality() + " should be " + 0);
        System.out.println(t_6.diff(t_6).cardinality() + " should be " + 0);
        System.out.println(t_6.diff(t_4).cardinality() + " should be " + 1);
        System.out.println(t_6.diff(t_8).cardinality() + " should be " + 1);
        System.out.println("");

        System.out.println("equal Tests:");
        System.out.println(MT.equal(MT) + " should be " + true);
        System.out.println(t_5.equal(t_4) + " should be " + false);
        System.out.println(t_5.equal(t_5) + " should be " + true);
        System.out.println(t_6.equal(t_4) + " should be " + false);
        System.out.println(t_6.equal(t_6) + " should be " + true);
        System.out.println(t_6.equal(t_7) + " should be " + false);
        System.out.println(t_6.equal(t_5) + " should be " + false);
        System.out.println("");

        System.out.println("subset Tests:");
        System.out.println(MT.subset(MT) + " should be " + true);
        System.out.println(t_5.subset(t_4) + " should be " + false);
        System.out.println(t_5.subset(t_5) + " should be " + true);
        System.out.println(t_4.subset(t_6) + " should be " + false);
        System.out.println(t_6.subset(t_6) + " should be " + true);
        System.out.println(t_7.subset(t_6) + " should be " + true);
        System.out.println(t_5.subset(t_6) + " should be " + true);
        System.out.println("");

// Just Random BST Things
        int randyInt = randomInt(0, 100);

        int length1 = randomInt(0, 10);
        int length2 = randomInt(0, 20);
        int length3 = randomInt(0, 30);
        int length4 = randomInt(0, 40);
        int length5 = randomInt(0, 50);

        System.out.println(length1);
        System.out.println(length2);
        System.out.println(length3);
        System.out.println(length4);
        System.out.println(length5);
        System.out.println("");

        BST randa = randomBST(length1);
        BST rande = randomBST(length2);
        BST randi = randomBST(length3);
        BST rando = randomBST(length4);
        BST randu = randomBST(length5);

        System.out.println(randa.cardinality());
        System.out.println(rande.cardinality());
        System.out.println(randi.cardinality());
        System.out.println(rando.cardinality());
        System.out.println(randu.cardinality());
        System.out.println("");
    }
}
