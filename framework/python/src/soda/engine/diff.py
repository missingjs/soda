def with_list(L1, L2):
    if len(L1) != len(L2):
        print('length not match')
        return

    not_match = []
    for i in range(len(L1)):
        if L1[i] != L2[i]:
            not_match.append(i)

    if not_match:
        print('list diff')
        for index in not_match:
            print(f'[{index}] {L1[index]} | {L2[index]}')
