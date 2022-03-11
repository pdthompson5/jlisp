;;This test verifies that lists are passed by value, not reference
(set test (list 1 2 3))
(set test2 test)

test 
test2

(set test (cdr test))
(set test (cons 15 test))

test 
test2
