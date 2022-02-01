require 'set'
# step [1]: implement solution function
# @param {String[][]} equations
# @param {Float[]} values
# @param {String[][]} queries
# @return {Float[]}
def calc_equation(equations, values, queries)
  index_map = get_index_map(equations)
  _N = index_map.size
  table = Array.new(_N) { Array.new(_N, -1.0) }

  (0...values.size).each { |k|
    p = equations[k]
    i = index_map[p[0]]
    j = index_map[p[1]]
    table[i][j] = values[k]
    table[j][i] = 1.0 / values[k]
  }

  res = Array.new(queries.size, 0.0)
  (0...res.size).each { |i|
    a = queries[i][0]
    b = queries[i][1]
    if !index_map.key?(a) || !index_map.key?(b)
      res[i] = -1.0
      next
    end
    ai = index_map[a]
    bi = index_map[b]
    if ai == bi
      res[i] = 1.0
      next
    end
    visited = Array.new(_N, false)
    res[i] = dfs(ai, bi, table, visited)
  }
  res
end

def get_index_map(eqs)
  imap = {}
  eqs.each { |e|
    a, b = e
    if !imap.key?(a)
      imap[a] = imap.size
    end
    if !imap.key?(b)
      imap[b] = imap.size
    end
  }
  imap
end

def dfs(ai, bi, table, visited)
  if table[ai][bi] >= 0.0
    return table[ai][bi]
  end

  visited[ai] = true
  res = -1.0
  (0...table.size).each { |adj|
    if table[ai][adj] >= 0.0 && !visited[adj]
      v = dfs(adj, bi, table, visited)
      if v >= 0.0
        res = table[ai][adj] * v
        break
      end
    end
  }
  table[ai][bi] = res
  table[bi][ai] = 1.0 / res
  res
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:calc_equation))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  # work.compare_serial = true
  puts work.run(ARGF.read)
end

