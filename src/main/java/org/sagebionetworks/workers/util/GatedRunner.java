package org.sagebionetworks.workers.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple runner controlled with a gate.
 * 
 * When {@link GatedRunner#run()} is called the wrapped runner will be
 * {@link Runnable#run()} if the provide {@link Gate#canRun()} returns true.
 *
 */
public class GatedRunner implements Runnable {

	private Logger log = LogManager.getLogger(GatedRunner.class);
	Gate gate;
	Runnable runner;

	public GatedRunner(Gate gate, Runnable runner) {
		super();
		if (gate == null) {
			throw new IllegalArgumentException("Gate cannot be null");
		}
		if (runner == null) {
			throw new IllegalArgumentException("Runner cannot be null");
		}
		this.gate = gate;
		this.runner = runner;
	}

	@Override
	public void run() {
		if (gate.canRun()) {
			try {
				runner.run();
			} catch (Exception e) {
				gate.runFailed(e);
			}
		} else {
			log.info(gate.getClass().getSimpleName() + " is closed for " + runner.getClass().getSimpleName());
		}
	}

}
