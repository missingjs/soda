export class TreeNode {
    val: number
    left: TreeNode | null
    right: TreeNode | null
    constructor(val?: number, left?: TreeNode | null, right?: TreeNode | null) {
        this.val = (val===undefined ? 0 : val)
        this.left = (left===undefined ? null : left)
        this.right = (right===undefined ? null : right)
    }
}

export class TreeFactory {
    static create(data: Array<number | null>): TreeNode | null {
        if (data.length === 0) {
            return null;
        }
        let root = new TreeNode(data[0]!);
        let qu = [root];
        for (let i = 1; i < data.length; ) {
            let node = qu.shift()!;
            if (data[i] !== null) {
                node.left = new TreeNode(data[i]!);
                qu.push(node.left);
            }
            if (++i === data.length) {
                break;
            }
            if (data[i] !== null) {
                node.right = new TreeNode(data[i]!);
                qu.push(node.right);
            }
            ++i;
        }
        return root;
    }

    static dump(root: TreeNode | null): Array<number | null> {
        if (!root) {
            return [];
        }
        let res = [];
        let qu = [root];
        while (qu.length > 0) {
            let node = qu.shift();
            if (node) {
                res.push(node.val);
                qu.push(node.left!);
                qu.push(node.right!);
            } else {
                res.push(null);
            }
        }
        while (!res.at(-1)) {
            res.pop();
        }
        return res;
    }
}