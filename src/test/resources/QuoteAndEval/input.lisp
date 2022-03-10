(set expressions (quote ((+ 1 2)(print 15)(set a 20))))
(set i 0)
(while (< i 3)
    (begin
        (set expression (car expressions))
        (eval expression)
        (set expressions (cdr expressions))
        (set i (+ i 1))
    )
)
a