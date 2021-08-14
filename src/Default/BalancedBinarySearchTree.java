package Default;

import java.util.HashSet;

/**
 * BST working with the Comparable implementation
 * @author moffa
 *
 * @param <E> The type of element we are storing into the tree which have to implement the Comparable interface
 */
public class BalancedBinarySearchTree <E extends Comparable<E>> {
	private E data;
	protected int height = 0;
	private BalancedBinarySearchTree<E> left = null;
	private BalancedBinarySearchTree<E> right = null;
	
	
	public BalancedBinarySearchTree(){}
	
	@Override
	public boolean equals(Object o){
		if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalancedBinarySearchTree<?> other = (BalancedBinarySearchTree<?>)o;
        
        // Leaf : only check data
        if(getLeft().isEmpty() && getRight().isEmpty()){
        	return getData().equals(other.getData());
        }
        
        // Not a leaf, check both sub trees are the same
        return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight());
	}
	
	@Override
	public int hashCode(){
		if(isEmpty()) return 0;
		int hash = getData().hashCode();
		hash = hash * 31 + getLeft().hashCode();
		hash = hash * 31 + getRight().hashCode();
		return hash;
	}
	
	/**
	 * Change the data value contained in the tree
	 * @param elem The new element
	 */
	public void setData(E elem){
		data = elem;
	}
	
	/**
	 * Return the data contained in the tree
	 * @return The element E
	 */
	public E getData(){
		return data;
	}
	
	/**
	 * Insert an element E into the tree in an equilibrated way
	 * @param elem The element to insert
	 */
	public void insert(E elem){
		if(isEmpty()){
			insertEmpty(elem);
		}else{
			int comp = elem.compareTo(getData());
			if(comp < 0){
				getLeft().insert(elem);
			}else if(comp > 0){
				// elem is greater, insert right
				getRight().insert(elem);
			}
			equilibrate();
		}
	}
	
	/**
	 * Get the number of node in the whole tree
	 * @return An integer value
	 */
	public int getSize() {
		if(getData() == null) return 0;
		return 1 + getLeft().getSize() + getRight().getSize();
	}
	
	/**
	 * Remove the max Element from the tree
	 * @return The removed Element
	 */
	public E deleteMax() {
		if (isEmpty()) 
			return null;
		else if (getRight().isEmpty()) {
			E data = getData();
			deleteRoot();
			return data;
		}
		else 	
			return getRight().deleteMax();
	}
	
	/**
	 * Remove an element E from the tree
	 * @param elem The element to Remove
	 */
	public void delete(E elem){
		if (!isEmpty()) {
			int comp = elem.compareTo(getData());
			if (comp > 0) 
				getRight().delete(elem);
			else if (comp < 0)
				getLeft().delete(elem);
			else
				deleteRoot();
			equilibrate();
		}
	}
	
	/**
	 * Remove the root node and equilibrate the tree
	 */
	private void deleteRoot(){
		if (getLeft().isEmpty()) {
			BalancedBinarySearchTree<E> t = getRight();
			setData(t.getData());
			setLeft(t.getLeft());
			setRight(t.getRight());
		}
		else if (getRight().isEmpty()) {
			BalancedBinarySearchTree<E> t = getLeft();
			setData(t.getData());
			setRight(t.getRight());
			setLeft(t.getLeft());
		}
		else
			setData(getRight().deleteMin());
		equilibrate();
	}
	
	/**
	 * Remove the min Element from the tree
	 * @return The removed Element
	 */
	private E deleteMin(){
		E minimum;
		if (getLeft().isEmpty()) {
			minimum = getData();
			BalancedBinarySearchTree<E> t = getRight();
			setData(t.getData());
			setLeft(t.getLeft());
			setRight(t.getRight());
		}else
			minimum = getLeft().deleteMin();
		equilibrate();
		return minimum;
	}
	
	/**
	 * Used to equilibrate the tree after adding or removing some element
	 */
	private void equilibrate(){
		if (getBalance() == 2) 
			if (getRight().getBalance() >= 0)
				leftRotation();
			else {
				getRight().rightRotation();
				leftRotation();
			}
		else if (getBalance() == -2) 
			if (getLeft().getBalance() <= 0)
				rightRotation();
			else {
				getLeft().leftRotation();
				rightRotation();
			}
		else computeHeight();
	}
	
	/**
	 * Left rotation of the tree ; used to equilibrate the tree
	 */
	private void leftRotation() {
		E elem = getData();
		BalancedBinarySearchTree<E> t = getRight();
		setData(t.getData());
		setRight(t.getRight());
		t.setData(elem);
		t.setRight(t.getLeft());
		t.setLeft(getLeft());
		setLeft(t);
		t.computeHeight();
		computeHeight();
	}

	/**
	 * Right rotation of the tree ; used to equilibrate the tree
	 */
	private void rightRotation() {
		E elem = getData();
		BalancedBinarySearchTree<E> t = getLeft();
		setData(t.getData());
		setLeft(t.getLeft());
		t.setData(elem);
		t.setLeft(t.getRight());
		t.setRight(getRight());
		setRight(t);
		t.computeHeight();
		computeHeight();
	}
	
	/**
	 * Compute the tree's height and store it
	 */
	public void computeHeight() {
		if (isEmpty()) 
			height = 0;
		else 
			height = 1 + Math.max(getLeft().getHeight(), getRight().getHeight());
	}
	
	/**
	 * Called if the tree is empty
	 * @param elem The element we want to add
	 */
	protected void insertEmpty(E elem) {
		setData(elem);
		setLeft(new BalancedBinarySearchTree<>());
		setRight(new BalancedBinarySearchTree<>());
		height = 1;
	}
	
	
	/**
	 * Search for an element E and return its instance
	 * @param elem The E element we are looking for
	 * @return Element E or null if not found
	 */
	public E search(E elem){
		Pair<BalancedBinarySearchTree<E>, HashSet<E>> e = searchTree(elem);
		if(e == null) return null;
		return e.getKey().getData();
	}
	
	/**
	 * Public version of the searchTree method
	 * @param elem The E element we are looking for
	 * @return  A pair containing the tree containing elem as first value and the set of all values found before reaching elem
	 */
	public Pair<BalancedBinarySearchTree<E>, HashSet<E>> searchTree(E elem){
		return searchTree(elem, new HashSet<E>());
	}
	
	/**
	 * Search for an element E
	 * @param elem The E element we are looking for
	 * @param set The set of element found before reaching elem
	 * @return A pair containing the tree containing elem as first value and the set of all values found before reaching elem
	 */
	private Pair<BalancedBinarySearchTree<E>, HashSet<E>> searchTree(E elem, HashSet<E> set){
		if(isEmpty()) return new Pair<BalancedBinarySearchTree<E>, HashSet<E>>(this, set);
		int comp = elem.compareTo(getData());
		set.add(getData());
		if(comp == 0){ // =
			return new Pair<BalancedBinarySearchTree<E>, HashSet<E>>(this, set);
		}else if(comp > 0){
			return getRight().searchTree(elem, set);
		}
		return getLeft().searchTree(elem, set);
	}
	
	
	/**
	 * Return the Left tree
	 * @return A BST
	 */
	public BalancedBinarySearchTree<E> getLeft(){
		return left;
	}
	
	/**
	 * Return the right tree
	 * @return A BST
	 */
	public BalancedBinarySearchTree<E> getRight(){
		return right;
	}
	
	
	/**
	 * Set the Left tree
	 * @param t A BST
	 */
	public void setLeft(BalancedBinarySearchTree<E> t){
		left = t;
	}
	
	/**
	 * Set the right tree
	 * @param t A BST
	 */
	public void setRight(BalancedBinarySearchTree<E> t){
		right = t;
	}
	
	/**
	 * Return the height of the tree
	 * @return An integer
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Return Whether the tree has no sub trees and no data
	 * @return A boolean value
	 */
	public boolean isEmpty(){
		return left == null && right == null && data == null;
	}
	
	/**
	 * Return the balance of the tree
	 * @return An integer value
	 */
	public int getBalance() {
		if (isEmpty()) 
			return 0;
		return getRight().getHeight() - getLeft().getHeight();
	}
	
	
}
