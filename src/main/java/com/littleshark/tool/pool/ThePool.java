package com.littleshark.tool.pool;

import com.littleshark.tool.exception.PoolIsEmptyException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//存放Ping对象
public class ThePool<T> {
    private final List<T> more;
    private final Stack<T> less;

    public ThePool(){
        more= Collections.synchronizedList(new LinkedList<>());
        less=new Stack<>();
    }

    /**
     * 获取池中的对象
     */
    public T getOne() {
        if (less.isEmpty()){
            if (more.isEmpty()){
                throw new PoolIsEmptyException("当前池中没有元素");
            }else addToStack();
        }
        return less.pop();
    }

    private void addToStack(){
        for (int i = 0; i < 20; i++) {
            less.add(more.remove(0));
            if (more.isEmpty())break;
        }
    }

    /**
     * 将obj存入池对象中
     * @param obj 对象
     */
    public void addToPool(T obj){
        more.add(obj);
    }

    /**
     * 判断池中是否为空
     */
    public boolean isEmpty(){
        return less.isEmpty()&& more.isEmpty();
    }

    /**
     * 进行获取池中的数量
     */
    public Integer getSize(){
        return less.size()+ more.size();
    }

}
