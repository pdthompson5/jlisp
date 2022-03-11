(set a 2) 
(set b 4)
(set bList (quote (1 2 3)))

;Basic Arithmetic:
;6
;-2
;8
;.5
(+ a b)
(- a b)
(* a b)
(/ a b)


(= a b)
(= a bList)
(= a 2)
(> a b)
(> b a)
(< a b)
(< b a)

;Comparisons
;()
;()
;t
;()
;t
;t
;()

;; (if (> 1 2) (t) (()))
;; (if (> 2 1) (t) (()))
;If statments
;()
;t

;While
(set i 0)
(while (< i 10) (set i (+ i 1)))
;9

;set
(set atomA 1)
atomA
(set listA (list 1 2 3))
listA
(set symbolA (quote a))

;1
;(1 2 3)

;Begin
(set a 1)
(begin (set a 15) (set a 10))
a
;10

;Cons 
(cons 1 2)
(cons 0 (list 1 2 3))
(cons 1 ())

;(1 . 2)
;(0 1 2 3)
;(1)

;Car
(car (cons 1 2))
(car (list 1 2 3))

;1
;1

;cdr
(cdr (cons 1 2))
(cdr (list 1 2 3))

;2
;(2 3)

(number? (list 1 2 3))
(number? (quote (+ 1 2)))
(number? 1)

;()
;()
;t

(symbol? 1)
(symbol? (quote (+ 1 2)))
(symbol? (quote a))

;()
;()
;t

(list? (cons 1 ()))
(list? (list 1 2 3))
(list? (quote (1 2 3)))
(list? 1)
(list? (quote (+ 1 2)))

;t
;t
;t
;()
;()

(null? (> 1 2))
(null? 1)
;t
;()

(print 1)
(print (list 1 2 3))

;1
;(1 2 3)



t
;t

()
;()


;functions define and call
(define foo () (print 1))
(define bar (a b c) (- a (+ b c)))

(foo)
(bar 1 2 3)

;1
;-4