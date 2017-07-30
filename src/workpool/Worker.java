package workpool;

/**
 * A thread that process tasks of both map and reduce
 * @author JetLi
 *
 */
public class Worker extends Thread {

	WorkPool wp;

	public Worker(WorkPool wp) {
		this.wp = wp;
	}

	void work(Task ps) throws Exception {
		ps.map();
	}
	/**
	 * As long as tasks list is not finished the worker asks for a task and process the task  
	 */
	public void run() {

		while (true) {
			Task ps = wp.getWork();
			if (ps == null)
				break;
			try {
				work(ps);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
