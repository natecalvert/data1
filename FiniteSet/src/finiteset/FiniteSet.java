package finiteset;

import java.util.*;

public class FiniteSet {

    interface BST{
        public int cardinality();
        public boolean isEmptyHuh();
        public boolean member( int elt);
        public BST add( int elt);
        public BST remove( int elt);
        public BST union( BST tree);
        public BST inter( BST tree);
        public BST diff( BST tree);
        public boolean equal( BST tree);
        public boolean subset( BST tree);
    }
    
    static class Empty implements BST {
        
        Empty(){ }
        
        public int cardinality(){
            return 0;
        }
        
        public boolean isEmptyHuh(){
            return true;
        }
        
        public boolean member( int elt){
            return false;
        }
        
        public BST add( int elt){
            return new Tree( elt, new Empty(), new Empty());
        }
        
        public BST remove( int elt){
            return new Empty();
        }
        
        public BST union( BST tree){
            return tree;
        }
        
        public BST inter( BST tree){
            return new Empty();
        }
        
        public BST diff( BST tree){
            return tree;
        }
        
        public boolean equal( BST tree){
            return tree.isEmptyHuh();
        }
        
        public boolean subset( BST tree){
            return tree.isEmptyHuh();
        }
    }
    
    static class Tree implements BST {
        int data;
        BST left;
        BST right;
        
        Tree (int data, BST left, BST right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
        
        public BST empty(){
            return new Empty();
        }
        
        public int cardinality(){
            return 1 + left.cardinality() + right.cardinality();
        }
        
        public boolean isEmptyHuh(){
            return false;
        }
        
        public boolean member( int elt){
            return (data == elt || left.member(elt) || right.member(elt));
        }
             
        public BST add( int elt){
            if (data == elt) {
                return this;
            } else if (elt < data){
                return new Tree(data, left.add(elt), right);
            } else {
                return new Tree(data, left, right.add(elt));
            }
        }
             
        public BST remove( int elt){
            if (elt == data) {
                return left.union(right);
            } else if (elt < data) {
                return new Tree(data, left.remove(elt), right);
            } else {
                return new Tree(data, left, right.remove(elt));
            }
        }
        
        public BST union( BST tree){
            return this.left.union(tree.union(this.right).add(this.data));
        }
        
        public BST inter( BST tree){
            if (tree.member(data)){
                return new Tree(data, left.inter(tree), right.inter(tree));
            } else {
                return left.inter(tree).union(right.inter(tree));
            }
        }

// diff should return what is in this and not in tree, currently returns
// what is in this and not in tree as well as what is in tree and not in this
        
        public BST diff( BST tree){
            if (tree.member(data)) {
                return left.diff(tree).union(right.diff(tree));
            } else {
                return new Tree(data, left.diff(tree), right.diff(tree));
            }
        }

// this one is also not working        
        
        public boolean equal( BST tree){
            return (this.subset(tree) && tree.subset(this));
        }
        
        public boolean subset( BST tree){
            return (tree.member(data) && left.subset(tree) && right.subset(tree));
        }
    }
            
    public static void main(String[] args) {
        
// CHECK BST PROPERTY IS HELD THROUGHOUT
        
        System.out.println("Gonna do some testing below:");
        System.out.println("");

// Very basic testing, checking that things seem to be okay. NOT EXHAUSTIVE. No fun properties, yet.

        BST MT = new Empty();
        BST t_4 = new Tree( 4, MT, MT);
        BST t_5 = new Tree( 5, MT, MT);
        BST t_7 = new Tree( 7, MT, MT);
        BST t_8 = new Tree( 8, MT, MT);
        BST t_6 = new Tree( 6, t_5, t_7);
       
        
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

// From here on is not working well. 
        //Due to specs of diff. Need to change. Meh.
        //Also subset is no bueno so equal is also no bueno since it uses subset.
        
        System.out.println("diff Tests:");
        System.out.println(MT.diff(MT).cardinality() + " should be " + 0);
        System.out.println(MT.diff(t_5).cardinality() + " should be " + 1);
        System.out.println(t_5.diff(MT).cardinality() + " should be " + 0);
        System.out.println(t_5.diff(t_4).cardinality() + " should be " + 1);
        System.out.println(t_5.diff(t_5).cardinality() + " should be " + 0);
        System.out.println(t_5.diff(t_6).cardinality() + " should be " + 2);
        System.out.println(t_6.diff(t_4).cardinality() + " should be " + 1);
        System.out.println(t_6.diff(t_5).cardinality() + " should be " + 1);
        System.out.println(t_6.diff(t_6).cardinality() + " should be " + 0);
        System.out.println(t_6.diff(t_7).cardinality() + " should be " + 1);
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
        System.out.println(t_6.subset(t_4) + " should be " + false);
        System.out.println(t_6.subset(t_6) + " should be " + true);
        System.out.println(t_6.subset(t_7) + " should be " + true);
        System.out.println(t_6.subset(t_5) + " should be " + true);
        System.out.println("");
    }
}
