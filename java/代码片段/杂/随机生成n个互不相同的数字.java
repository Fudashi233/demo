	/**
	 * 从low到high中随机生成n个互不相同的数
	 * @param low
	 * @param high
	 * @param n
	 * @return
	 */
	public static int[] random(int low,int high,int n) {
		
		if(high<low) {
			
			throw new RuntimeException("参数high需要大于low");
		}
		if(high-low+1<n) {
			
			throw new RuntimeException("无法生成"+n+"个互不相同的随机数");
		}
		int[] arr = new int[high-low+1];
		/*初始化数组*/
		for(int i=0;i<arr.length;i++) {
			arr[i] = i+low;
		}
		shuffle(arr);
		int[] result = new int[n];
		for(int i=0;i<n;i++) {
			result[i] = arr[i];
		}
		return result;
	}
	
	/*打乱数组*/
	public static void shuffle(int[] arr) {
		
		for(int i=0;i<arr.length;i++) {
			
			/*随机生成一个索引*/
			int j = i+(int)(Math.random()*(arr.length-i));
			/*交换i、j*/
			int temp = arr[i];
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}