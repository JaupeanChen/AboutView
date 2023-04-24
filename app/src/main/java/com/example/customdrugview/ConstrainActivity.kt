package com.example.customdrugview

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class ConstrainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_rv)
        val adapter = MyAdapter(listOf("摄像头1", "摄像头2", "摄像头3"), this)
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = adapter
//        rv.layoutManager = LinearLayoutManager(this)
        val text = findViewById<TextView>(R.id.text)
        val text2 = findViewById<TextView>(R.id.text2)
        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            text2.isVisible = !text2.isVisible
        }

        val arr = arrayOf(4, 2, 9, 3, 1, 6)
//        val arr = arrayOf(1, 3, 6, 5, 7, 9)
//        val arr = arrayOf(9, 8, 6, 5, 3, 1)
        Log.d("打印", "开始排序前：${arr.contentToString()}")
//        val newArr = mergeSort(arr, 0, 5)
//        sort(arr, 0, arr.size - 1)
//        quickSort(arr, 0, arr.size - 1)
        Log.d("打印", "排序后：${arr.contentToString()}")
        hano('J', 'A', 'Y', 3)
    }

    fun hano(a: Char, b: Char, c: Char, n: Int) {
        if (n > 0) {
            hano(a, c, b, n - 1)
            Log.d("打印", "$a -> $c")
            hano(b, a, c, n - 1)
        }
    }


    fun quickSort(arr: Array<Int>, lo: Int, hi: Int) {
        if (lo >= hi) return  //始终要有递归结束条件
        val pivot = partition(arr, lo, hi)
        quickSort(arr, lo, pivot - 1)
        quickSort(arr, pivot + 1, hi)
    }

    fun partition(arr: Array<Int>, lo: Int, hi: Int): Int {
        Log.d("打印", "进行分区：${lo} - $hi")
        Log.d("打印", "取到基准数为：${arr[lo]}")
//        var pivot = lo //取第一个值为基准数
        val pivot = arr[lo] //取第一个值为基准数
        var i = lo
        var j = hi
        while (i < j) {
            //基值在左边，所以从右边比起
            while (i < j && arr[j] > pivot) {
                //这里应该是循环到要去对换位置，然后循环结束掉
                j--
            }
            //直到遇到j位置上的数小于基准数，进行对换
            if (i < j) {
//                swap(arr, j, pivot)  //因为最后留下的坑肯定是基准值的坑，所以不用每次都对换
//                //此时基准值移到j位置
//                pivot = j
                arr[i] = arr[j]
                i++ //当前i位置被基准值占了，下次比较从后一位开始
            }

            //基准值在右边，跟左边的比
            while (i < j && arr[i] < arr[pivot]) {
                i++
            }
            //直到遇到i位置上的数大于基准数，进行对换
            if (i < j) {
//                swap(arr, i, pivot)
//                pivot = i
                arr[j] = arr[i]
                j--
            }
        }
        arr[i] = pivot  //当本轮都比完之后，也就是i和j相遇，把基准值丢入坑即可
        Log.d("打印", "结束分区：基准数${arr[pivot]}, 下标为${pivot}")
        Log.d("打印", "该次分区结果：${arr.contentToString()}")
        return pivot
    }

    fun swap(arr: Array<Int>, i: Int, j: Int) {
        val temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }

    //插入排序
    fun insertionSort(arr: Array<Int>) {
        for (j in 1 until arr.size) {
            Log.d("打印", "取第${j}个数:${arr[j]}对比")
            //这边循环也就是拿出被比的数
            var r = j      //当前被比的数（下标）
            var l = j - 1  //每次开始比的左边数（下标）
            while (l >= 0) {
                if (arr[l] > arr[r]) {
                    //交换位置
                    val temp = arr[l]
                    arr[l--] = arr[r]
                    arr[r--] = temp
                    Log.d("打印", "发生交换：${arr.contentToString()}")
                } else {
                    if (l == j - 1) {
                        Log.d("打印", "本轮无需交换")
                    }
                    break
                }
            }
        }
    }

    fun insertionSort2(arr: Array<Int>) {
        for (i in 1 until arr.size) {
            var j = i - 1
            val current = arr[i] //取到本轮被比的数
            Log.d("打印", "本轮比较取数为：${current}")
            while (j >= 0 && arr[j] > current) {
                arr[j + 1] = arr[j] //移坑，这里移完之后并没有做交换，是因为存了current，后面都比较完再填坑即可
                Log.d("打印", "发生交换：坑${j}移到坑${j + 1}后 ${arr.contentToString()}")
                j--
            }
            //每次移坑（如果有）之后j会自减1，所以这里j需要补1回来；没移坑的话，current本来就是j+1坑里的数
            arr[j + 1] = current //最后把current补到移走的坑里
            Log.d("打印", "交换完成补坑：${arr.contentToString()}")
        }
    }

    fun sort(nums: IntArray) {
        var i = 1
        var j: Int
        var current: Int
        while (i < nums.size) {
            current = nums[i]
            j = i - 1
            while (j >= 0 && nums[j] > current) {
                nums[j + 1] = nums[j]
                j--
            }
            nums[j + 1] = current
            i++
        }
    }

    fun mergeSort(arr: Array<Int>, head: Int, tail: Int): Array<Int> {
        if (head >= tail) return arr  //递归结束条件
        val mid = head + (tail - head) / 2
        mergeSort(arr, head, mid)
        mergeSort(arr, mid + 1, tail)
        return merge(arr, head, mid, tail)
    }

    fun merge(arr: Array<Int>, head: Int, mid: Int, tail: Int): Array<Int> {
        Log.d("打印", "进行merge, head:${head}, mid: ${mid}, tail: $tail")
        val size = tail - head + 1  //本次比较的数组成员数
        val printArr = Array(size) { it.inc() }  //?竟然没办法直接用size去初始化Array？
        val tempArr = arr.clone()
        var i = head      //左边合并组起始指针
        var j = mid + 1   //右边合并组起始指针

        //这里index每次都从0开始递增，直接赋给arr当下标肯定会有问题
        //所以需要重新定义一个字段k, 让它从原数组要比较的开始下标开始递增
        var k = head
        for (index in 0 until size) {
            if (i > mid) {
                printArr[index] = tempArr[j]
                //当左边项取完后，直接取右边项即可
                arr[k++] = tempArr[j++]
                continue
            }
            if (j > tail) {
                printArr[index] = tempArr[i]
                //当右边项取完后，直接取左边项即可
                arr[k++] = tempArr[i++]
                continue
            }
            if (tempArr[i] < tempArr[j]) {
                printArr[index] = tempArr[i]
                //取左边
                arr[k++] = tempArr[i++]
            } else if (tempArr[i] > tempArr[j]) {
                printArr[index] = tempArr[j]
                //取右边
                arr[k++] = tempArr[j++]
            }
        }
        Log.d("打印", "该次merge结果：${printArr.contentToString()}")
//        Log.d("打印", "该次merge结果：${arr.contentToString()}")
        return arr
    }

    //归并排序
    fun sort(arr: Array<Int>, lo: Int, hi: Int) {
        if (lo >= hi) return
        val mid = lo + (hi - lo) / 2
        sort(arr, lo, mid)
        sort(arr, mid + 1, hi)
        mergeSeg(arr, lo, mid, hi)
    }

    fun mergeSeg(num: Array<Int>, lo: Int, mid: Int, hi: Int) {
        Log.d("打印", "进行merge, head:${lo}, mid: ${mid}, tail: $hi")

        val size = hi - lo + 1
        var index = 0
        Log.d("打印", "size:$size")
        val printArr = Array(size) { it.inc() }

        val copy = num.clone()
        var k = lo
        var i = lo       //左边合并组起始指针
        var j = mid + 1  //右边合并组起始指针
        while (k <= hi) {  //其实就是循环当前数组个数
            Log.d("打印", "k:$k")
            if (i > mid) {
                printArr[index++] = copy[j]
                num[k++] = copy[j++]
            } else if (j > hi) {
                printArr[index++] = copy[i]
                num[k++] = copy[i++]
            } else if (copy[i] < copy[j]) {
                printArr[index++] = copy[i]
                num[k++] = copy[i++]
            } else if (copy[i] > copy[j]) {
                printArr[index++] = copy[j]
                num[k++] = copy[j++]
            }
        }
        Log.d("打印", "该次merge结果：${printArr.contentToString()}")
        Log.d("打印", "该次merge原数组结果：${num.contentToString()}")
    }

    //冒泡
    fun bubbleSort(arr: Array<Int>): Array<Int> {
        var hasChange = true
        //循环次数，当比较完size-1个数之后，最后一个自然也就不用再比了，所以只需要n-1次。
        for (i in 1 until arr.size) {  //until不包含尾数
            if (!hasChange) break
            Log.d("打印", "第${i}轮冒泡")
            hasChange = false
            //每次循环的两两冒泡比较，经过i次循环之后，确定的i个数也就不用比了。
            for (index in 0..(arr.size - 1 - i)) {
                val next = index + 1
                Log.d("打印", "两两冒泡${index},${next}")
                if (arr[index] > arr[next]) {
                    val temp = arr[index]
                    arr[index] = arr[next]
                    arr[next] = temp
                    hasChange = true  //优化循环，如果当前轮没有发生位置移动，那说明排序完成，即可退出
                }
            }
            Log.d("打印", "第${i}轮冒泡结果：${arr.contentToString()}")
        }
        return arr
    }

}