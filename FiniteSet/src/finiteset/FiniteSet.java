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
        // Specifications provided are those from assignment
        // for clarity in use when creating tests.

        //Constuctor for Empty class
        Empty() {
        }

        // (empty) → finite-set
        // Returns a fresh empty set.
        public BST empty() {
            return new Empty();
        }

        // (cardinality t) → integer
        //      t : finite-set
        // Returns the number of elements in t.
        public int cardinality() {
            return 0;
        }

        // (isEmptyHuh t) → boolean
        //      t : finite-set
        // Determines if t is empty.
        public boolean isEmptyHuh() {
            return true;
        }

        // (member t elt) → boolean
        //      t : finite-set
        //      elt : integer
        // Determines if elt is in t.
        public boolean member(int elt) {
            return false;
        }

        // (add t elt) → finite-set
        //      t : finite-set
        //      elt : integer
        // Returns a set containing elt and everything in t.
        public BST add(int elt) {
            return new Tree(elt, new Empty(), new Empty());
        }

        // (remove t elt) → finite-set
        //      t : finite-set
        //      elt : integer
        // Returns a set containing everything in t except elt.
        public BST remove(int elt) {
            return new Empty();
        }

        // (union t u) → finite-set
        //      t : finite-set
        //      u : finite-set
        // Returns a set containing everything in t and u.
        public BST union(BST tree) {
            return tree;
        }

        // (inter t u) → finite-set
        //      t : finite-set
        //      u : finite-set
        // Returns a set containing everything that is in both t and u.
        public BST inter(BST tree) {
            return new Empty();
        }

        // (diff t u) → finite-set
        //      t : finite-set
        //      u : finite-set
        // Returns a set containing everything in u except those that are in t.
        public BST diff(BST tree) {
            return tree;
        }

        // (equal t u) → boolean
        //      t : finite-set
        //      u : finite-set
        // Determines if t and u contain the same elements.
        public boolean equal(BST tree) {
            return tree.isEmptyHuh();
        }

        // (subset t u) → boolean
        //      t : finite-set
        //      u : finite-set
        // Determines if t is a subset of u.
        public boolean subset(BST tree) {
            return true;
        }
    }

    static class Tree implements BST {
        // Specifications are as in Empty class. 

        int data;
        BST left;
        BST right;

        //Constructor for Tree class
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

// Functions to generate randomBSTs
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

// Functions used for testing:
    // Cardinality & isEmptyHuh
    //   If cardinality = 0, then isEmptyHuh must return true.
    //   If cardinality != 0, then isEmptyHuh must return false.
    public static void cardinality_isEmptyHuh_test(BST tree) {
        if (tree.cardinality() == 0) {
            if (tree.isEmptyHuh()) {
                //System.out.println("cardinality_isEmptyHuh_test Pass!");
            } else {
                System.out.println("cardinality_isEmptyHuh_test Fail!");
            }
        } else if (!tree.isEmptyHuh()) {
            //System.out.println("cardinality_isEmptyHuh_test Pass!");
        } else {
            System.out.println("cardinality_isEmptyHuh_test Fail!");
        }
    }

    // Cardinality & Remove
    //   After removing and element from a BST, the cardinality must
    //   be less than or equal to the original cardinality.
    public static void remove_cardinality_test(BST tree, int elt) {
        if (tree.remove(elt).cardinality() <= tree.cardinality()) {
            //System.out.println("remove_cardinality_test Pass!");
        } else {
            System.out.println("remove_cardinality_test Fail!");
        }
    }

    // Cardinality & Add
    //   After adding and element to a BST, the cardinality must
    //   be greater than or equal to the original cardinality.
    public static void add_cardinality_test(BST tree, int elt) {
        if (tree.add(elt).cardinality() >= tree.cardinality()) {
            //System.out.println("add_cardinality_test Pass!");
        } else {
            System.out.println("add_cardinality_test Fail!");
        }
    }

    // Remove & Member
    //   After removing and element from a BST, member must 
    //   return false when called on the removed element.
    public static void remove_member_test(BST tree, int elt) {
        if (tree.remove(elt).member(elt) == false) {
            //System.out.println("remove_member_test Pass!");
        } else {
            System.out.println("remove_member_test Fail!");
        }
    }

    // Union, Member & Add
    //   If an element is added to two BSTs, the union of the BSTs after
    //   the addition must contain the elemtent, so member must return true.
    public static void union_member_test(BST tree1, BST tree2, int elt) {
        BST temp1 = tree1.add(elt);
        BST temp2 = tree2.add(elt);
        BST union1 = temp1.union(temp2);
        BST union2 = temp2.union(temp1);
        if (union1.member(elt) && union2.member(elt)) {
            //System.out.println("union_member_test Pass!");
        } else {
            System.out.println("union_member_test Fail!");
        }
    }

    // Diff, Equal & isEmptyHuh
    //    If two BTSs are equal, the difference of the two must be empty.
    //    If two BTSs are not equal, the difference of the two is non-empty.
    public static void difference_isEmptyHuh_test(BST tree1, BST tree2) {
        if (tree1.equal(tree2)) {
            if (tree1.diff(tree2).isEmptyHuh()
                    && tree2.diff(tree1).isEmptyHuh()) {
                //System.out.println("difference_isEmptyHuh_test Pass!");
            } else {
                System.out.println("difference_isEmptyHuh_test Fail!");
            }
        } else if (!tree1.diff(tree2).isEmptyHuh()
                || !tree2.diff(tree1).isEmptyHuh()) {
            //System.out.println("difference_isEmptyHuh_test Pass!");
        } else {
            System.out.println("difference_isEmptyHuh_test Fail!");
        }
    }

    // Equal, Diff & isEmptyHuh [opposite of previous]
    //    If the difference of two BSTs is empty, the BTSs must be equal.
    //    If the difference of two BSts is non-empty, the BSTs must be non-equal.
    public static void equal_isEmptyHuh_test(BST tree1, BST tree2) {
        if (tree1.diff(tree2).isEmptyHuh()
                && tree2.diff(tree1).isEmptyHuh()) {
            if (tree1.equal(tree2)) {
                //System.out.println("difference_isEmptyHuh_test Pass!");
            } else {
                System.out.println("difference_isEmptyHuh_test Fail!");
            }
        } else if (!tree1.equal(tree2)) {
            //System.out.println("difference_isEmptyHuh_test Pass!");
        } else {
            System.out.println("difference_isEmptyHuh_test Fail!");
        }
    }

    // Inter, Member, Add & isEmptyHuh
    //    If an element is in two BSTs, then the intersect of the BSTs
    //    must be non-empty.
    public static void inter_member_test(BST tree1, BST tree2, int elt) {
        BST temp1 = tree1.add(elt);
        BST temp2 = tree2.add(elt);
        if (temp1.member(elt) && temp2.member(elt)) {
            if (!temp1.inter(temp2).isEmptyHuh()) {
                //System.out.println("inter_member_test Pass!");
            } else {
                System.out.println("inter_member_test Fail!");
            }
        } else {
            System.out.println("Add in inter_member_test Fail!");
        }
    }

    // Subset & Union
    //   For three BSTs, the subset of the thrid on the union of the first two
    //   must equal the subset of the thrid on the first and the subset of the
    //   third on the second.
    public static void subset_union_test(BST tree1, BST tree2, BST tree3) {
        if ((tree1.union(tree2)).subset(tree3)
                == (tree1.subset(tree3) && tree2.subset(tree3))) {
            //System.out.println("subset_union_test Pass!");
        } else {
            System.out.println("subset_union_test Fail!");
        }
    }

    public static void main(String[] args) {

        System.out.println("Only failed tests print messages.");
        System.out.println("If no messages follow, no tests failed!");
        System.out.println("");

// Testing using randomBSTs and properties
        for (int i = 0; i < 1000; i++) {
            int x = randomInt(0, 20);
            int y = randomInt(0, 20);

            int lengtht = randomInt(0, 10);
            int lengthu = randomInt(0, 10);

            BST t = randomBST(lengtht);
            BST u = randomBST(lengthu);

            cardinality_isEmptyHuh_test(t);
            cardinality_isEmptyHuh_test(u);

            remove_cardinality_test(t, x);
            remove_cardinality_test(u, y);
            remove_cardinality_test(t, y);
            remove_cardinality_test(u, x);

            add_cardinality_test(t, x);
            add_cardinality_test(u, y);
            add_cardinality_test(t, y);
            add_cardinality_test(u, x);

            remove_member_test(t, x);
            remove_member_test(u, y);
            remove_member_test(t, y);
            remove_member_test(u, x);

            union_member_test(t, u, x);
            union_member_test(u, t, y);
            union_member_test(t, u, y);
            union_member_test(u, t, x);

            difference_isEmptyHuh_test(t, u);
            difference_isEmptyHuh_test(u, t);

            equal_isEmptyHuh_test(t, u);
            equal_isEmptyHuh_test(u, t);

            inter_member_test(t, u, x);
            inter_member_test(u, t, y);
            inter_member_test(t, u, y);
            inter_member_test(u, t, x);

            int lengthv = randomInt(0, 10);
            BST v = randomBST(lengthv);

            subset_union_test(t, u, v);
            subset_union_test(t, v, u);
            subset_union_test(u, t, v);
            subset_union_test(u, v, t);
            subset_union_test(v, t, u);
            subset_union_test(v, u, t);
        }

// Basic testing
//    Used for inital checks of functions.
//        BST MT = new Empty();
//        BST t_4 = new Tree(4, MT, MT);
//        BST t_5 = new Tree(5, MT, MT);
//        BST t_7 = new Tree(7, MT, MT);
//        BST t_8 = new Tree(8, MT, MT);
//        BST t_6 = new Tree(6, t_5, t_7);
//
//        System.out.println("cardinality() Tests:");
//        System.out.println(MT.cardinality() + " should be " + 0);
//        System.out.println(t_5.cardinality() + " should be " + 1);
//        System.out.println(t_6.cardinality() + " should be " + 3);
//        System.out.println("");
//
//        System.out.println("isEmptyHuh() Tests:");
//        System.out.println(MT.isEmptyHuh() + " should be " + true);
//        System.out.println(t_5.isEmptyHuh() + " should be " + false);
//        System.out.println(t_6.isEmptyHuh() + " should be " + false);
//        System.out.println("");
//
//        System.out.println("member() Tests:");
//        System.out.println(MT.member(5) + " should be " + false);
//        System.out.println(t_5.member(5) + " should be " + true);
//        System.out.println(t_5.member(6) + " should be " + false);
//        System.out.println(t_6.member(5) + " should be " + true);
//        System.out.println(t_6.member(7) + " should be " + true);
//        System.out.println(t_6.member(6) + " should be " + true);
//        System.out.println(t_6.member(8) + " should be " + false);
//        System.out.println(t_6.member(4) + " should be " + false);
//        System.out.println("");
//
//        System.out.println("add() Tests:");
//        System.out.println(MT.add(5).cardinality() + " should be " + 1);
//        System.out.println(t_5.add(4).cardinality() + " should be " + 2);
//        System.out.println(t_5.add(8).cardinality() + " should be " + 2);
//        System.out.println(t_6.add(4).cardinality() + " should be " + 4);
//        System.out.println(t_6.add(8).cardinality() + " should be " + 4);
//        System.out.println(MT.add(5).member(5) + " should be " + true);
//        System.out.println(t_5.add(4).member(4) + " should be " + true);
//        System.out.println(t_5.add(8).member(8) + " should be " + true);
//        System.out.println(t_6.add(4).member(4) + " should be " + true);
//        System.out.println(t_6.add(8).member(8) + " should be " + true);
//        System.out.println("");
//
//        System.out.println("remove() Tests:");
//        System.out.println(MT.remove(5).cardinality() + " should be " + 0);
//        System.out.println(t_5.remove(4).cardinality() + " should be " + 1);
//        System.out.println(t_5.remove(5).cardinality() + " should be " + 0);
//        System.out.println(t_6.remove(4).cardinality() + " should be " + 3);
//        System.out.println(t_6.remove(6).cardinality() + " should be " + 2);
//        System.out.println(t_6.remove(7).cardinality() + " should be " + 2);
//        System.out.println(t_6.remove(5).cardinality() + " should be " + 2);
//        System.out.println("");
//
//        System.out.println("union() Tests:");
//        System.out.println(MT.union(MT).isEmptyHuh() + " should be " + true);
//        System.out.println(MT.union(t_4).isEmptyHuh() + " should be " + false);
//        System.out.println(MT.union(t_4).cardinality() + " should be " + 1);
//        System.out.println(t_5.union(t_4).cardinality() + " should be " + 2);
//        System.out.println(t_5.union(t_7).cardinality() + " should be " + 2);
//        System.out.println(t_5.union(t_8).cardinality() + " should be " + 2);
//        System.out.println(t_5.union(t_7).union(t_8).cardinality() + " should be " + 3);
//        System.out.println(t_5.union(t_8).union(t_7).cardinality() + " should be " + 3);
//        System.out.println(t_7.union(t_8).union(t_5).cardinality() + " should be " + 3);
//        System.out.println(t_7.union(t_5).union(t_8).cardinality() + " should be " + 3);
//        System.out.println(t_8.union(t_7).union(t_5).cardinality() + " should be " + 3);
//        System.out.println(t_8.union(t_5).union(t_7).cardinality() + " should be " + 3);
//        System.out.println(t_6.union(t_4).cardinality() + " should be " + 4);
//        System.out.println(t_6.union(t_8).cardinality() + " should be " + 4);
//        System.out.println(t_6.union(t_8).union(t_4).cardinality() + " should be " + 5);
//        System.out.println(t_6.union(t_4).union(t_8).cardinality() + " should be " + 5);
//        System.out.println("");
//
//        System.out.println("inter() Tests:");
//        System.out.println(MT.inter(MT).cardinality() + " should be " + 0);
//        System.out.println(MT.inter(t_5).cardinality() + " should be " + 0);
//        System.out.println(t_5.inter(t_4).cardinality() + " should be " + 0);
//        System.out.println(t_5.inter(t_5).cardinality() + " should be " + 1);
//        System.out.println(t_5.inter(t_6).cardinality() + " should be " + 1);
//        System.out.println(t_6.inter(t_4).cardinality() + " should be " + 0);
//        System.out.println(t_6.inter(t_5).cardinality() + " should be " + 1);
//        System.out.println(t_6.inter(t_6).cardinality() + " should be " + 3);
//        System.out.println(t_6.inter(t_7).cardinality() + " should be " + 1);
//        System.out.println(t_6.inter(t_8).cardinality() + " should be " + 0);
//        System.out.println("");
//
//        System.out.println("diff() Tests:");
//        System.out.println(MT.diff(MT).cardinality() + " should be " + 0);
//        System.out.println(MT.diff(t_5).cardinality() + " should be " + 1);
//        System.out.println(t_5.diff(t_5).cardinality() + " should be " + 0);
//        System.out.println(t_5.diff(t_6).cardinality() + " should be " + 2);
//        System.out.println(t_5.diff(t_4).cardinality() + " should be " + 1);
//        System.out.println(MT.diff(t_6).cardinality() + " should be " + 3);
//        System.out.println(t_6.diff(MT).cardinality() + " should be " + 0);
//        System.out.println(t_6.diff(t_5).cardinality() + " should be " + 0);
//        System.out.println(t_6.diff(t_7).cardinality() + " should be " + 0);
//        System.out.println(t_6.diff(t_6).cardinality() + " should be " + 0);
//        System.out.println(t_6.diff(t_4).cardinality() + " should be " + 1);
//        System.out.println(t_6.diff(t_8).cardinality() + " should be " + 1);
//        System.out.println("");
//
//        System.out.println("equal() Tests:");
//        System.out.println(MT.equal(MT) + " should be " + true);
//        System.out.println(t_5.equal(t_4) + " should be " + false);
//        System.out.println(t_5.equal(t_5) + " should be " + true);
//        System.out.println(t_6.equal(t_4) + " should be " + false);
//        System.out.println(t_6.equal(t_6) + " should be " + true);
//        System.out.println(t_6.equal(t_7) + " should be " + false);
//        System.out.println(t_6.equal(t_5) + " should be " + false);
//        System.out.println("");
//
//        System.out.println("subset() Tests:");
//        System.out.println(MT.subset(MT) + " should be " + true);
//        System.out.println(t_5.subset(t_4) + " should be " + false);
//        System.out.println(t_5.subset(t_5) + " should be " + true);
//        System.out.println(t_4.subset(t_6) + " should be " + false);
//        System.out.println(t_6.subset(t_6) + " should be " + true);
//        System.out.println(t_7.subset(t_6) + " should be " + true);
//        System.out.println(t_5.subset(t_6) + " should be " + true);
//        System.out.println("");
    }
}
