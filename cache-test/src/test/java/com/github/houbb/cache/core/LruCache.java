package com.github.houbb.cache.core;

import java.util.HashMap;

public class LruCache {
    private int cap;

    private HashMap<Integer, DListNode> cache;

    private DListNode head;

    private DListNode tail;

    int count;

    public LruCache(int cap){
        this.cap = cap;
        this.count = 0;

        cache = new HashMap<>();
        head = new DListNode();
        head.pre = null;

        tail = new DListNode();
        tail.next = null;

        head.next = tail;
        tail.pre = head;
    }

    public Integer update(Integer key){
        DListNode node = cache.get(key);
        if (node == null){
            return -1;
        }

        this.moveToHead(node);

        return node.value;
    }

    public void put(Integer key, Integer value){
        DListNode node = cache.get(key);
        if (node == null){
            DListNode newNode = new DListNode();
            newNode.key = key;
            newNode.value = value;

            this.cache.put(key, newNode);
            moveToHead(newNode);
            ++count;

            if(count > cap){
                DListNode evictNode = this.popTail();
                this.cache.remove(evictNode.key);
                --count;
            }
        }else {
            node.value = value;
            this.moveToHead(node);
        }
    }

    public void moveToHead(DListNode node){
        this.removeNode(node);
        this.addNode(node);
    }

    public void removeNode(DListNode node){
        DListNode pre = node.pre;
        DListNode next = node.next;

        pre.next = next;
        next.pre = pre;
    }

    public void addNode(DListNode node){
        node.pre = head;
        node.next = head.next;

        head.next.pre = node;
        head.next = node;
    }

    public DListNode popTail(){
        DListNode res = tail.pre;
        this.removeNode(res);

        return res;
    }


}
