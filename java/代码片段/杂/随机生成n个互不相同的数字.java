	/**
	 * ��low��high���������n��������ͬ����
	 * @param low
	 * @param high
	 * @param n
	 * @return
	 */
	public static int[] random(int low,int high,int n) {
		
		if(high<low) {
			
			throw new RuntimeException("����high��Ҫ����low");
		}
		if(high-low+1<n) {
			
			throw new RuntimeException("�޷�����"+n+"��������ͬ�������");
		}
		int[] arr = new int[high-low+1];
		/*��ʼ������*/
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
	
	/*��������*/
	public static void shuffle(int[] arr) {
		
		for(int i=0;i<arr.length;i++) {
			
			/*�������һ������*/
			int j = i+(int)(Math.random()*(arr.length-i));
			/*����i��j*/
			int temp = arr[i];
			arr[i] = arr[j];
			arr[j] = temp;
		}
	}