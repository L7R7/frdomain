package frdomain.ch3
package algebra

import java.util.Date

import scala.util.Try

trait AccountService[Account, Amount, Balance] {
  def open(no: String, name: String, openingDate: Option[Date]): Try[Account]
  def close(account: Account, closeDate: Option[Date]): Try[Account]
  def debit(account: Account, amount: Amount): Try[Account]
  def credit(account: Account, amount: Amount): Try[Account]
  def balance(account: Account): Try[Balance]

  def transfer(from: Account, to: Account, amount: Amount): Try[(Account, Account, Amount)] = for {
    a <- debit(from, amount)
    b <- credit(to, amount)
  } yield (a, b, amount)
}

trait AccountServiceOptional[Account, Amount, Balance] {
  def open(no: String, name: String, openingDate: Option[Date]): Option[Account]

  def close(account: Account, closeDate: Option[Date]): Option[Account]

  def debit(account: Account, amount: Amount): Option[Account]

  def credit(account: Account, amount: Amount): Option[Account]

  def balance(account: Account): Option[Balance]

  def transfer(from: Account, to: Account, amount: Amount): Option[(Account, Account, Amount)] = for {
    a <- debit(from, amount)
    b <- credit(to, amount)
  } yield (a, b, amount)
}

//trait AccountServiceM[Account, Amount, Balance, M[_]] {
//  def open(no: String, name: String, openingDate: Option[Date]): M[Account]
//  def close(account: Account, closeDate: Option[Date]): M[Account]
//  def debit(account: Account, amount: Amount): M[Account]
//  def credit(account: Account, amount: Amount): M[Account]
//  def balance(account: Account): M[Balance]
//
//  def transfer(from: Account, to: Account, amount: Amount): M[(Account, Account, Amount)] = for {
//    a <- debit(from, amount)
//    b <- credit(to, amount)
//  } yield (a, b, amount)
//}


/*
- Option vs Try: Option loses information about what went wrong, it can only tell whether an action was successful or not
- Try vs. Either: try is tailored for exceptions, Either can be used with any kind of type for the description of failures
 */