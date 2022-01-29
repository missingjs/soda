#include "util.h"

using namespace std;

namespace soda::unittest {

string Utils::fromStdin()
{
    std::string line, content;
    while (std::getline(std::cin, line)) {
        content += line;
    }
    return content;
}

} // namespace soda::unittest
