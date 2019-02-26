package frdomain.ch3

import frdomain.ch3.lens.{Address, AddressLenses, Customer, CustomerLenses}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}

object LensLaws extends AddressLenses with CustomerLenses {

  val genNo = Gen.oneOf("B-12", "C-17", "D-80")
  val genStreet = Gen.oneOf("Monroe Street", "San Juan Street", "Camac Street")
  val genCity = Gen.oneOf("denver", "san jose", "chicago")
  val genState = Gen.oneOf("CA", "IL", "CO")
  val genZip = Gen.numStr

  val validAddress = for {
    no <- genNo
    st <- genStreet
    ct <- genCity
    st <- genState
    zp <- genZip
  } yield Address(no, st, ct, st, zp)

  implicit val arbitraryAddress: Arbitrary[Address] = Arbitrary {
    validAddress
  }

  val genCustNo = Gen.oneOf(1, 2, 3)
  val genName = Gen.oneOf("John", "David", "Mary")

  val validCustomer = for {
    no <- genCustNo
    nm <- genName
    ac <- arbitrary[Address]
  } yield Customer(no, nm, ac)

  implicit val arbitraryCustomer: Arbitrary[Customer] = Arbitrary {
    validCustomer
  }

  val lensIdentityLawCheck = for {
    c <- arbitrary[Customer]
  } yield addressLens.set(c, addressLens.get(c)) == c

  val lensRetentionLawCheck = for {
    c <- arbitrary[Customer]
    a <- arbitrary[Address]
  } yield addressLens.get(addressLens.set(c, a)) == a

  val lensDoubleSetLawCheck = for {
    c <- arbitrary[Customer]
    a <- arbitrary[Address]
    b <- arbitrary[Address]
  } yield addressLens.get(addressLens.set(addressLens.set(c, a), b)) == b
}

object LensLawsSpecification extends Properties("LensLaws") {

  import LensLaws._

  property("Checking Lens Identity Law") = forAll(lensIdentityLawCheck)(_ == true)
  property("Checking Lens Retention Law") = forAll(lensRetentionLawCheck)(_ == true)
  property("Checking Lens Double Set Law") = forAll(lensDoubleSetLawCheck)(_ == true)
}
