package test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicFloat {
	
	private AtomicInteger bits;
	
	public AtomicFloat() {
		bits = new AtomicInteger();
	}
	
	public AtomicFloat(float value) {
		bits = new AtomicInteger(Float.floatToIntBits(value));
	}
	
	public float get() {
		return Float.intBitsToFloat(bits.get());
	}
	
	public void set(float value) {
		bits.set(Float.floatToIntBits(value));
	}
	
	public void lazySet(float value) {
		bits.lazySet(Float.floatToIntBits(value));
	}
	
	public float getAndSet(float value) {
		
		float expect = 0;
		do {
			expect = get();
		} while(!compareAndSet(expect,Float.floatToIntBits(value)));
		return expect;
	}

	public boolean compareAndSet(float expect,float update) {
		return bits.compareAndSet(Float.floatToIntBits(expect),Float.floatToIntBits(update));
	}
	
	public float getAndAdd(float delta) {
		
		float expect = 0;
		float update = 0;
		do {
			expect = get();
			update = expect+delta;
		} while(!compareAndSet(expect,update));
		return expect;
	}
	
	public float addAndGet(float delta) {
		
		float expect = 0;
		float update = 0;
		do {
			expect = get();
			update = expect+delta;
		} while(!compareAndSet(expect,update));
		return update;
	}
	
	/**
	 * @return
	 * i++
	 */
	public float  getAndIncrement() {
		
		return getAndAdd(1);
	}
	
	/**
	 * @return
	 * i--
	 */
	public float getAndDecrement() {
		return addAndGet(-1);
	}
	
	/**
	 * @return
	 * ++i
	 */
	public float incrementAndGet() {
		return addAndGet(1);
	}
	
	/**
	 * @return
	 * --i
	 */
	public float decrementAndGet() {
		return addAndGet(-1);
	}
	
	public float floatValue() {
		return get();
	}
	
	public double doubleValue() {
		return (double)get();
	}
	
	public int intValue() {
		return (int)get();
	}
	
	public long longValue() {
		return (long)get();
	}
	
	public String toString() {
		return Float.toString(get());
	}
}
