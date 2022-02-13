function TreeNode(val, left, right) {
    this.val = (val===undefined ? 0 : val)
    this.left = (left===undefined ? null : left)
    this.right = (right===undefined ? null : right)
}

class TreeFactory {
    static create(data) {
        if (data.length === 0) {
            return null;
        }
        let root = new TreeNode(data[0]);
        let qu = [root];
        for (let i = 1; i < data.length; ) {
            let node = qu.shift();
            if (data[i]) {
                node.left = new TreeNode(data[i]);
                qu.push(node.left);
            }
            if (++i === data.length) {
                break;
            }
            if (data[i]) {
                node.right = new TreeNode(data[i]);
                qu.push(node.right);
            }
            ++i;
        }
        return root;
    }

    static dump(root) {
        if (!root) {
            return [];
        }
        let res = [];
        let qu = [root];
        while (qu.length > 0) {
            let node = qu.shift();
            if (node) {
                res.push(node.val);
                qu.push(node.left);
                qu.push(node.right);
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

exports.TreeNode = TreeNode;
exports.TreeFactory = TreeFactory;
