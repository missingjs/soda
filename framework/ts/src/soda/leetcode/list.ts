export class ListNode {
    val: number
    next: ListNode | null
    constructor(val?: number, next?: ListNode | null) {
        this.val = (val===undefined ? 0 : val)
        this.next = (next===undefined ? null : next)
    }
}

export class ListFactory {
    static create(data: number[]): ListNode | null {
        let head = new ListNode();
        let tail = head;
        data.forEach(val => {
            let node = new ListNode(val);
            tail.next = node;
            tail = node;
        });
        return head.next;
    }   

    static dump(head: ListNode | null): number[] {
        let list = [];
        while (head) {
            list.push(head.val);
            head = head.next;
        }
        return list;
    }
}
