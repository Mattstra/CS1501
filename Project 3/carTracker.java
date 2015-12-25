import java.util.Scanner;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class carTracker
{

	private void displayMenu()
	{
		System.out.println("Enter an option number: \n");
		System.out.println("[1] Add a car \n");
		System.out.println("[2] Update a car \n");
		System.out.println("[3] Remove a car \n");
		System.out.println("[4] Get lowest priced car \n");
		System.out.println("[5] Get lowest mileage car \n");
		System.out.println("[6] Get lowest priced car by make and model \n");
		System.out.println("[7] Get lowest mileage car by make and model \n");
	}


	public carTracker()
	{
	}


	public static void main(String[] args)
	{
		int option;
		int option2;
		int ind = 0;		//keeps track on index into pq
		int deletes = 0;
		boolean repeat = false;
		String readVin;
		carTracker cTrack = new carTracker();
		carInfo car = new carInfo();
		String yesOrNo;
		IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(10);
		IndexMinPQ<Integer> pq2 = new IndexMinPQ<Integer>(10);

		do
		{
			cTrack.displayMenu();

			//user enters a number
			Scanner scan1 = new Scanner(System.in);
			option = scan1.nextInt();

			//add cars to priority queues
			if(option == 1)
			{
				car.addCar();
				pq.insert(ind, car.mileages[ind]);
				pq2.insert(ind, car.prices[ind]);
				ind++;
			}

			//update cars
			if(option == 2)
			{
				System.out.println("Enter the VIN of the car you wish to update");
				readVin = scan1.next();

				for(int i = 0; i < car.index; i++)
				{
					if(car.carArray[i][0].equals(readVin))
					{
						//found matching vin
						System.out.println("Enter number of what you wish to update");
						System.out.println("[1] Price \n");
						System.out.println("[2] Mileage \n");
						System.out.println("[3] Color \n");
						option2 = scan1.nextInt();

						if(option2 == 1)
						{
							//get original price
							int origPrice = pq2.keyOf(i);
							System.out.println("Enter updated price");
							int newPrice = scan1.nextInt();
							//update price
							pq2.changeKey(i, newPrice);
						}

						if(option2 == 2)
						{
							//get original mileage
							int origMileage = pq.keyOf(i);
							System.out.println("Enter updated mileage");
							int newMileage = scan1.nextInt();
							//update mileage
							pq.changeKey(i, newMileage);
						}

						if(option2 == 3)
						{
							String newColor;
							System.out.println("Enter updated color");
							newColor = scan1.next();
							car.carArray[i][3] = newColor;
						}

						break;		//no need to continue
					}
				}
			}

			//removing cars
			if(option == 3)
			{
				boolean vinIsFound = false;
				boolean runAgain = false;
				String tryAgain;
				int modIndex;

				do
				{
					System.out.println("Enter the VIN of the car you wish to remove");
					readVin = scan1.next();

					for(int i = 0; i < car.index; i++)
					{
						if(car.carArray[i][0].equals(readVin))
						{
							//found matching vin
							//want to remove car i so shift all indices(i) > i down by 1
							//also need to remove car from both priority queues
							vinIsFound = true;
							modIndex = i+deletes;		//keeps track of priority queue indexes since they can get misaligned with array

							for(int j = car.index; i < j; i++)
							{

								if(j == 1)
								{
									car.carArray[i] = null;
									break;
								}

								else
								{
									car.carArray[i] = car.carArray[i+1];
								}

							}

							if(vinIsFound == true)
							{
								if(car.carArray[i] != null)
								{
									pq.delete(modIndex);
									pq2.delete(modIndex);
								}

								if(car.carArray[i] == null)
								{
									pq.delete(i);
									pq2.delete(i);
								}

								System.out.println("Specified car has been removed");
								runAgain = false;
								break;
							}
						}
				}

				if(vinIsFound != true)
				{
					System.out.println("Entered VIN not found. Try Again? (y/n) \n");
					tryAgain = scan1.next();
					if(tryAgain.equals("y"))
						runAgain = true;
					if(tryAgain.equals("n"))
						runAgain = false;
				}
			}
			while(runAgain == true);

				deletes++;

		}

			//retrieving lowest priced car
			if(option == 4)
			{
				//need index of min key for getting car info from array
				int minPrice = pq2.minIndex();
				System.out.println("Lowest priced car is the " + car.carArray[minPrice][1] + " " + car.carArray[minPrice][2] + " at " + "$" + pq2.minKey());

			}

			//retrieve lowest mileage car
			if(option == 5)
			{
				//need index of min key for getting car info from array
				int minMileage = pq.minIndex();
				System.out.println("Lowest mileage car is the " + car.carArray[minMileage][1] + " " + car.carArray[minMileage][2] + " at " + pq.minKey() + " " +"miles");

			}

			//retrieve lowest priced car by make and model
			if(option == 6)
			{
				int x = 0;
				int[] priceList = new int[car.index];
				int[] indexList = new int[car.index];
				int minPrice = 0;
				int currInd = 0;
				String carMake;
				String carModel;
				System.out.println("Enter the make of the car to be retrieved \n");
				carMake = scan1.next();
				System.out.println("Enter the model of the car to be retrieved \n");
				carModel = scan1.next();

				for(int i =0; i < car.index; i++)
				{
					if(car.carArray[i][1].equals(carMake) && car.carArray[i][2].equals(carModel))
					{
						priceList[x] = pq2.keyOf(i);
						indexList[x] = i;
						x++;
					}
				}

				for(int j = 0; j < priceList.length; j++)
				{
					if(priceList[j] == 0)
					{
					}

					else
					{
						if(j == 0)
						{
							minPrice = priceList[j];
							currInd = indexList[j];
						}

						if((j!= 0)&&(priceList[j] < minPrice))
						{
							minPrice = priceList[j];
							currInd = indexList[j];
						}

						if((j!= 0)&&(priceList[j] >= minPrice))
						{

						}
					}
				}

				System.out.println("Lowest priced " + car.carArray[currInd][1] + " " + car.carArray[currInd][2] + " is " + "$" + minPrice);
				System.out.println("Vin: " + car.carArray[currInd][0] + "\n" + "Color: " + car.carArray[currInd][3] + "\n" + "Mileage: " + pq.keyOf(currInd) + " miles");

			}

			//retrieve lowest mileage car by make and model
			if(option == 7)
			{
				int x = 0;
				int[] mileageList = new int[car.index];
				int[] indexList = new int[car.index];
				int minMileage = 0;
				int currInd = 0;
				String carMake;
				String carModel;
				System.out.println("Enter the make of the car to be retrieved \n");
				carMake = scan1.next();
				System.out.println("Enter the model of the car to be retrieved \n");
				carModel = scan1.next();

				for(int i =0; i < car.index; i++)
				{
					if(car.carArray[i][1].equals(carMake) && car.carArray[i][2].equals(carModel))
					{
						mileageList[x] = pq.keyOf(i);
						indexList[x] = i;
						x++;
					}
				}

				for(int j = 0; j < mileageList.length; j++)
				{
					if(mileageList[j] == 0)
					{
					}

					else
					{
						if(j == 0)
						{
							minMileage = mileageList[j];
							currInd = indexList[j];
						}

						if((j!= 0)&&(mileageList[j] < minMileage))
						{
							minMileage = mileageList[j];
							currInd = indexList[j];
						}

						if((j!= 0)&&(mileageList[j] >= minMileage))
						{

						}
					}
				}

				System.out.println("Lowest mileage " + car.carArray[currInd][1] + " " + car.carArray[currInd][2] + " has " + minMileage + " miles");
				System.out.println("Vin: " + car.carArray[currInd][0] + "\n" + "Price: " + "$" + pq2.keyOf(currInd) + "\n" +  "Color: " + car.carArray[currInd][3]);

			}

			//ask user if they are done
			System.out.println("Are you finished? (y/n)");
			yesOrNo = scan1.next();
			if(yesOrNo.equals("n"))
				repeat = true;
			if(yesOrNo.equals("y"))
				repeat = false;
		}
		while(repeat == true);
	}


	public static class carInfo
	{
		Scanner scan2 = new Scanner(System.in);
		int numCar;
		String vin, make, model, color;
		int mileage;
		int price;
		int index = 0;
		int[] mileages = new int[10];
		int[] prices = new int[10];
		String[][] carArray = new String[10][4];
		boolean tryAgain = false;

		public void addCar()
		{
			do
			{
				System.out.println("Enter the 17 character VIN which contains numbers and capital letters:");
				System.out.println("The following characters cannot be part of the VIN: I, O, Q \n");
				vin = scan2.next();

				if((vin.length() != 17) || (vin.indexOf('O') != -1) || (vin.indexOf('I') != -1) || (vin.indexOf('Q') != -1))
				{
					System.out.println("You entered an invalid VIN \n");
					tryAgain = true;
				}

				if((vin.length() == 17) || (vin.indexOf('O') == -1) || (vin.indexOf('I') == -1) || (vin.indexOf('Q') == -1))
				{
					tryAgain = false;
				}
			}
			while(tryAgain == true);


			System.out.println("Enter the make: \n");
			make = scan2.next();
			System.out.println("Enter the model: \n");
			model = scan2.next();
			System.out.println("Enter the price: \n");
			price = scan2.nextInt();
			prices[index] = price;
			System.out.println("Enter the mileage: \n");
			mileage = scan2.nextInt();
			mileages[index] = mileage;
			System.out.println("Enter the color: \n");
			color = scan2.next();

			//insert car info other than price and mileage into a 2d array
			for(int attribute = 0; attribute < 4; attribute++)
			{
				switch(attribute)
				{
				case 0:
					carArray[index][attribute] = vin;
					break;
				case 1:
					carArray[index][attribute] = make;
					break;
				case 2:
					carArray[index][attribute] = model;
					break;
				case 3:
					carArray[index][attribute] = color;
					break;
				default:
					break;
				}
			}


			index++;
		}

	}

	public static class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	    private int maxN;        // maximum number of elements on PQ
	    private int N;           // number of elements on PQ
	    private int[] pq;        // binary heap using 1-based indexing
	    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	    private Key[] keys;      // keys[i] = priority of i

	    /**
	     * Initializes an empty indexed priority queue with indices between <tt>0</tt>
	     * and <tt>maxN - 1</tt>.
	     * @param  maxN the keys on this priority queue are index from <tt>0</tt>
	     *         <tt>maxN - 1</tt>
	     * @throws IllegalArgumentException if <tt>maxN</tt> &lt; <tt>0</tt>
	     */
	    public IndexMinPQ(int maxN) {
	        if (maxN < 0) throw new IllegalArgumentException();
	        this.maxN = maxN;
	        keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
	        pq   = new int[maxN + 1];
	        qp   = new int[maxN + 1];                   // make this of length maxN??
	        for (int i = 0; i <= maxN; i++)
	            qp[i] = -1;
	    }


	    /**
	     * Returns true if this priority queue is empty.
	     *
	     * @return <tt>true</tt> if this priority queue is empty;
	     *         <tt>false</tt> otherwise
	     */
	    public boolean isEmpty() {
	        return N == 0;
	    }

	    /**
	     * Is <tt>i</tt> an index on this priority queue?
	     *
	     * @param  i an index
	     * @return <tt>true</tt> if <tt>i</tt> is an index on this priority queue;
	     *         <tt>false</tt> otherwise
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     */
	    public boolean contains(int i) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        return qp[i] != -1;
	    }

	    /**
	     * Returns the number of keys on this priority queue.
	     *
	     * @return the number of keys on this priority queue
	     */
	    public int size() {
	        return N;
	    }

	    /**
	     * Associates key with index <tt>i</tt>.
	     *
	     * @param  i an index
	     * @param  key the key to associate with index <tt>i</tt>
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws IllegalArgumentException if there already is an item associated
	     *         with index <tt>i</tt>
	     */
	    public void insert(int i, Key key) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
	        N++;
	        qp[i] = N;
	        pq[N] = i;
	        keys[i] = key;
	        swim(N);
	    }

	    /**
	     * Returns an index associated with a minimum key.
	     *
	     * @return an index associated with a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public int minIndex() {
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        return pq[1];
	    }

	    /**
	     * Returns a minimum key.
	     *
	     * @return a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public Key minKey() {
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        return keys[pq[1]];
	    }

	    /**
	     * Removes a minimum key and returns its associated index.
	     * @return an index associated with a minimum key
	     * @throws NoSuchElementException if this priority queue is empty
	     */
	    public int delMin() {
	        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
	        int min = pq[1];
	        exch(1, N--);
	        sink(1);
	        qp[min] = -1;            // delete
	        keys[pq[N+1]] = null;    // to help with garbage collection
	        pq[N+1] = -1;            // not needed
	        return min;
	    }

	    /**
	     * Returns the key associated with index <tt>i</tt>.
	     *
	     * @param  i the index of the key to return
	     * @return the key associated with index <tt>i</tt>
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
	     */
	    public Key keyOf(int i) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        else return keys[i];
	    }

	    /**
	     * Change the key associated with index <tt>i</tt> to the specified value.
	     *
	     * @param  i the index of the key to change
	     * @param  key change the key assocated with index <tt>i</tt> to this key
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
	     */
	    public void changeKey(int i, Key key) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        keys[i] = key;
	        swim(qp[i]);
	        sink(qp[i]);
	    }

	    /**
	     * Change the key associated with index <tt>i</tt> to the specified value.
	     *
	     * @param  i the index of the key to change
	     * @param  key change the key assocated with index <tt>i</tt> to this key
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @deprecated Replaced by {@link #changeKey(int, Key)}.
	     */
	    public void change(int i, Key key) {
	        changeKey(i, key);
	    }

	    /**
	     * Decrease the key associated with index <tt>i</tt> to the specified value.
	     *
	     * @param  i the index of the key to decrease
	     * @param  key decrease the key assocated with index <tt>i</tt> to this key
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws IllegalArgumentException if key &ge; key associated with index <tt>i</tt>
	     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
	     */
	    public void decreaseKey(int i, Key key) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) <= 0)
	            throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
	        keys[i] = key;
	        swim(qp[i]);
	    }

	    /**
	     * Increase the key associated with index <tt>i</tt> to the specified value.
	     *
	     * @param  i the index of the key to increase
	     * @param  key increase the key assocated with index <tt>i</tt> to this key
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws IllegalArgumentException if key &le; key associated with index <tt>i</tt>
	     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
	     */
	    public void increaseKey(int i, Key key) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        if (keys[i].compareTo(key) >= 0)
	            throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
	        keys[i] = key;
	        sink(qp[i]);
	    }

	    /**
	     * Remove the key associated with index <tt>i</tt>.
	     *
	     * @param  i the index of the key to remove
	     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	     * @throws NoSuchElementException no key is associated with index <t>i</tt>
	     */
	    public void delete(int i) {
	        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
	        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
	        int index = qp[i];
	        exch(index, N--);
	        swim(index);
	        sink(index);
	        keys[i] = null;
	        qp[i] = -1;
	    }


	   /***************************************************************************
	    * General helper functions.
	    ***************************************************************************/
	    private boolean greater(int i, int j) {
	        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	    }

	    private void exch(int i, int j) {
	        int swap = pq[i];
	        pq[i] = pq[j];
	        pq[j] = swap;
	        qp[pq[i]] = i;
	        qp[pq[j]] = j;
	    }


	   /***************************************************************************
	    * Heap helper functions.
	    ***************************************************************************/
	    private void swim(int k)  {
	        while (k > 1 && greater(k/2, k)) {
	            exch(k, k/2);
	            k = k/2;
	        }
	    }

	    private void sink(int k) {
	        while (2*k <= N) {
	            int j = 2*k;
	            if (j < N && greater(j, j+1)) j++;
	            if (!greater(k, j)) break;
	            exch(k, j);
	            k = j;
	        }
	    }


	   /***************************************************************************
	    * Iterators.
	    ***************************************************************************/

	    /**
	     * Returns an iterator that iterates over the keys on the
	     * priority queue in ascending order.
	     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
	     *
	     * @return an iterator that iterates over the keys in ascending order
	     */
	    public Iterator<Integer> iterator() { return new HeapIterator(); }

	    private class HeapIterator implements Iterator<Integer> {
	        // create a new pq
	        private IndexMinPQ<Key> copy;

	        // add all elements to copy of heap
	        // takes linear time since already in heap order so no keys move
	        public HeapIterator() {
	            copy = new IndexMinPQ<Key>(pq.length - 1);
	            for (int i = 1; i <= N; i++)
	                copy.insert(pq[i], keys[pq[i]]);
	        }

	        public boolean hasNext()  { return !copy.isEmpty();                     }
	        public void remove()      { throw new UnsupportedOperationException();  }

	        public Integer next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return copy.delMin();
	        }
	    }
	}

}
