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
	
	
	public BalancedBinarySearchTree(){
		
	}
	
	public void setData(E elem){
		data = elem;
	}
	
	public E getData(){
		return data;
	}
	
	public void insert(E elem){
		if(isEmpty()){
			insertEmpty(elem);
		}else{
			int comp = elem.compareTo(getData());
			if(comp == 0){
				// Do nothing for now
			}else if(comp == 1){
				// elem is greater, insert right
				getRight().insert(elem);
				equilibrate();
			}else{
				getLeft().insert(elem);
				equilibrate();
			}
		}
	}
	
	public int getSize() {
		if(getData() == null) return 0;
		return 1 + getLeft().getSize() + getRight().getSize();
	}
	
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
	
	public boolean isLeaf(){
		return getRight() == null && getLeft() == null;
	}
		
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
	
	public void equilibrate(){
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
	
	public void leftRotation() {
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

	public void rightRotation() {
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
	
	public void computeHeight() {
		if (isEmpty()) 
			height = 0;
		else 
			height = 1 + Math.max(getLeft().getHeight(), getRight().getHeight());
	}
	
	public void insertEmpty(E elem) {
		setData(elem);
		setLeft(new BalancedBinarySearchTree<>());
		setRight(new BalancedBinarySearchTree<>());
		height = 1;
	}
	
	public E search(E elem){
		Pair<BalancedBinarySearchTree<E>, HashSet<E>> e = searchTree(elem);
		if(e == null) return null;
		return e.getKey().getData();
	}
	
	public Pair<BalancedBinarySearchTree<E>, HashSet<E>> searchTree(E elem){
		return searchTree(elem, new HashSet<E>());
	}
	
	public Pair<BalancedBinarySearchTree<E>, HashSet<E>> searchTree(E elem, HashSet<E> set){
		if(isEmpty()) return null;
		int comp = elem.compareTo(getData());
		set.add(getData());
		if(comp == 0){ // =
			return new Pair<BalancedBinarySearchTree<E>, HashSet<E>>(this, set);
		}else if(comp > 0){
			if(getRight().getData() != null && getData().compareTo(getRight().getData()) > 1) {
				 getData().compareTo(getRight().getData());
				throw new RuntimeException();
			}
			return getRight().searchTree(elem, set);
		}
		if(getLeft().getData() != null && getData().compareTo(getLeft().getData()) < 1) {
			getData().compareTo(getLeft().getData());
			throw new RuntimeException();
		}
		return getLeft().searchTree(elem, set);
	}
	
	
	public BalancedBinarySearchTree<E> getLeft(){
		return left;
	}
	
	public BalancedBinarySearchTree<E> getRight(){
		return right;
	}
	
	public void setLeft(BalancedBinarySearchTree<E> t){
		left = t;
	}
	
	public void setRight(BalancedBinarySearchTree<E> t){
		right = t;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isEmpty(){
		return left == null && right == null && data == null;
	}
	
	public int getBalance() {
		if (isEmpty()) 
			return 0;
		return getRight().getHeight() - getLeft().getHeight();
	}
	
	
}
