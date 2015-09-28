package autoblacktest.actions.simple;

import java.util.Vector;

import resources.TesterHelper;

import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataTree;
import com.rational.test.ft.vp.ITestDataTreeNode;
import com.rational.test.ft.vp.ITestDataTreeNodes;

public class TreeUI extends TesterHelper {

	public static void click(TestObject testObject, long nodeToClick) {
		int selectedNode = (int)nodeToClick+1;
		boolean reachedNode = false;
		Vector<String> path = new Vector<String>();
		
		ITestDataTree iTreeData = (ITestDataTree)testObject.getTestData("tree");
		ITestDataTreeNodes iNodes = iTreeData.getTreeNodes();
		int nodeCount = iNodes.getNodeCount();

		System.out.println("node count = " + nodeCount);
		if( nodeCount != 0) {
			ITestDataTreeNode[] node = iNodes.getRootNodes();
//			System.out.println("node root = " + node.length + " " + selectedNode);
			for(int i = 0; i < node.length && !reachedNode; i++) {
				path.add(node[i].getNode().toString());
				if(path.size() == selectedNode) {
					new GuiSubitemTestObject(testObject).click(atPath(path.lastElement()));
					break;
				}
//				System.out.println(path.lastElement() + " " + path.size());
				reachedNode = exploreChild(path, node[i].getChildren(), selectedNode, testObject);
			}
		}
	}
	
	public static void doubleClick(TestObject testObject, long nodeToClick) {
		int selectedNode = (int)nodeToClick+1;
		boolean reachedNode = false;
		Vector<String> path = new Vector<String>();
		
		ITestDataTree iTreeData = (ITestDataTree)testObject.getTestData("tree");
		ITestDataTreeNodes iNodes = iTreeData.getTreeNodes();
		int nodeCount = iNodes.getNodeCount();
		
//		System.out.println("node count = " + nodeCount);
		if( nodeCount != 0) {
			ITestDataTreeNode[] node = iNodes.getRootNodes();
//			System.out.println("node root = " + node.length + " " + selectedNode);
			for(int i = 0; i < node.length && !reachedNode; i++) {
				path.add(node[i].getNode().toString());
				if(path.size() == selectedNode) {
					
					new GuiSubitemTestObject(testObject).doubleClick(atPath(path.lastElement()));
					break;
				}
//				System.out.println(path.lastElement() + " " + path.size());
				reachedNode = exploreChild(path, node[i].getChildren(), selectedNode, testObject);
			}
		}
	}
	
	private static boolean exploreChild (Vector<String> path, ITestDataTreeNode[] children, int selectedNode, TestObject testObject) {
		String father = path.lastElement();
		boolean reachedNode = false;
		
		for(int child = 0; child<children.length && !reachedNode; child++) {
			path.add(father + "->" + children[child].getNode().toString());
//			System.out.println(path.lastElement() + " " + path.size());
			if(path.size() == selectedNode) {
				new GuiSubitemTestObject(testObject).click(atPath(path.lastElement()));
				new GuiSubitemTestObject(testObject).click(atPath(path.lastElement()));
				return true;
			}
			reachedNode = exploreChild(path, children[child].getChildren(), selectedNode, testObject);
		}
		return false;
	}

}
