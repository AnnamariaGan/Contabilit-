use contabilita;

/*
insert into movimento (nomeutente, importo, data, voce, tipo) values
( 'anna@maria.it', 2000, '2020-05-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-05-05', 'affitto', 'u'),
( 'anna@maria.it', 50, '2020-05-15', 'viaggi', 'u'),
( 'anna@maria.it', 55, '2020-05-16', 'viaggi', 'u'),
( 'anna@maria.it', 100, '2020-05-17', 'spesa', 'u'),
( 'anna@maria.it', 30, '2020-05-28', 'cure mediche', 'u'),
( 'anna@maria.it', 2000, '2020-06-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-06-05', 'affitto', 'u'),
( 'anna@maria.it', 80, '2020-06-10', 'spesa', 'u'),
( 'anna@maria.it', 30, '2020-06-23', 'altro', 'u'),
( 'anna@maria.it', 50, '2020-07-01', 'spesa', 'u'),
( 'anna@maria.it', 2000, '2020-07-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-07-01', 'affitto', 'u'),
( 'anna@maria.it', 100, '2020-07-23', 'cure mediche', 'u'),
( 'anna@maria.it', 2000, '2020-08-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-08-03', 'affitto', 'u'),
( 'anna@maria.it', 35, '2020-08-14', 'spesa', 'u'),
( 'anna@maria.it', 20, '2020-08-29', 'spesa', 'u'),
( 'anna@maria.it', 2000, '2020-09-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-09-02', 'affitto', 'u'),
( 'anna@maria.it', 120, '2020-09-15', 'viaggi', 'u'),
( 'anna@maria.it', 40, '2020-09-16', 'altro', 'u'),
( 'anna@maria.it', 60, '2020-09-19', 'spesa', 'u'),
( 'anna@maria.it', 20, '2020-09-23', 'altro', 'u'),
( 'anna@maria.it', 40, '2020-09-29', 'cure mediche', 'u'),
( 'anna@maria.it', 2000, '2020-10-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2021-10-04', 'affitto', 'u'),
( 'anna@maria.it', 50, '2020-10-11', 'altro', 'u'),
( 'anna@maria.it', 2000, '2020-11-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-11-05', 'affitto', 'u'),
( 'anna@maria.it', 45, '2020-11-11', 'spesa', 'u'),
( 'anna@maria.it', 2000, '2020-12-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2020-12-01', 'affitto', 'u'),
( 'anna@maria.it', 55, '2020-12-01', 'spesa', 'u'),
( 'anna@maria.it', 100, '2020-12-10', 'cure mediche', 'u'), 
( 'anna@maria.it', 70, '2020-12-15', 'altro', 'u'),
( 'anna@maria.it', 100, '2020-12-20', 'altro', 'u'),
( 'anna@maria.it', 80, '2020-12-21', 'viaggi', 'u'),
( 'anna@maria.it', 30, '2020-12-22', 'spesa', 'u'),
( 'anna@maria.it', 20, '2020-12-22', 'spesa', 'u'),
( 'anna@maria.it', 2000, '2021-01-01', 'stipendio', 'e'),
( 'anna@maria.it', 500, '2021-01-03', 'affitto', 'u'),
( 'anna@maria.it', 20, '2021-01-04', 'spesa', 'u'),
( 'anna@maria.it', 100, '2021-01-04', 'altro', 'e'),
( 'anna@maria.it', 20, '2021-01-05', 'spesa', 'u'),
( 'anna@maria.it', 80, '2021-01-07', 'spesa', 'u');
 
 */
 
 select *
 from movimento;
 
 /*
 select *
 from movimento
 where nomeutente = "anna@maria.it" and data between '2020-10-01'and  '2020-11-01' ;
 */
 
 /*
 set @entrate = (   select  sum(importo) as entrate
                                    from movimento
                                    where nomeutente = "anna@maria.it" and tipo = 'e'
                                );
set @uscite = (select  sum(importo) as entrate
                                    from movimento
                                    where nomeutente = "anna@maria.it" and tipo = 'u'
                            );
set @saldo = @entrate - @uscite;
select @saldo;
*/

select voce, sum(importo) as totale
from movimento
where nomeutente = "anna@maria.it"
group by voce;




 
 
