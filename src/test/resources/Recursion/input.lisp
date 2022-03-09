;; Recursive fibonacci function
(define fib(n)
    (if (= n 0)
        (0)
        (if (= n 1)
            (1)
            (+ (fib (- n 1)) (fib (- n 2)))
        )
    )
)

(fib 8)

;; Reccursive function to count down from n to zero
(define countDown(n)
    (begin 
        (print n)
        (if (> n 0)
            (countDown (- n 1))
            n
        )
    )
)

(countDown 3)



;;Colinked recursion ->  functions can call each other regardless of their definition order
(define isOdd (n)
    (if (= n 0)
        ()
        (isEven (- n 1))
    )

)

(define isEven (n)
    (if (= n 0)
        t
        (isOdd (- n 1))
    )
)

(isOdd 3)
(isOdd 4)
(isEven 2)
(isEven 1)