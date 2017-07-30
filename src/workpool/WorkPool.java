package workpool;

import java.util.LinkedList;
/**
 * Class that creates a list of tasks and offers tasks to workers or put them on hold untill tasks list is populated 
 * @author JetLi
 *
 */
public class WorkPool {
	int nThreads;
	int nWaiting = 0;
	public boolean finish = false;
	LinkedList<Task> tasks = new LinkedList<Task>();

	public WorkPool(int nThreads) {
		this.nThreads = nThreads;
	}
	/**
	 * Offers tasks to workers or put them on hold until tasks list is populated
	 * @return - a task from list
	 */
	public synchronized Task getWork() {
		if (tasks.size() == 0) {
			nWaiting++;
			if (nWaiting == nThreads) {
				finish = true;
				notifyAll();
				return null;
			} else {
				while (!finish && tasks.size() == 0) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (finish) {
					return null;
				}
				nWaiting--;
			}
		}
		return tasks.remove();
	}
	/**
	 * Creates tasks list
	 * @param task - to be put on list
	 */
	public synchronized void putWork(Task task) {
		tasks.add(task);
		this.notify();
	}
}