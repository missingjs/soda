function ListNode(val, next) {
    this.val = (val===undefined ? 0 : val)
    this.next = (next===undefined ? null : next)
}

class ListFactory {
    static create(data) {
        let head = new ListNode();
        let tail = head;
        data.forEach(val => {
            let node = new ListNode(val);
            tail.next = node;
            tail = node;
        });
        return head.next;
    }

    static dump(head) {
        let list = [];
        while (head) {
            list.push(head.val);
            head = head.next;
        }
        return list;
    }
}

exports.ListNode = ListNode;
exports.ListFactory = ListFactory;
