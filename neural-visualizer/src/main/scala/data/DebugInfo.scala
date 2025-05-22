package data

import play.api.libs.json._

sealed trait Register
case class Rax(value: Long)                extends Register
case class Rbx(value: Long)                extends Register
case class Rcx(value: Long)                extends Register
case class Rdx(value: Long)                extends Register
case class Rsi(value: Long)                extends Register
case class Rdi(value: Long)                extends Register
case class Rbp(value: Long)                extends Register
case class Rsp(value: Long)                extends Register
case class R8(value: Long)                 extends Register
case class R9(value: Long)                 extends Register
case class R10(value: Long)                extends Register
case class R11(value: Long)                extends Register
case class R12(value: Long)                extends Register
case class R13(value: Long)                extends Register
case class R14(value: Long)                extends Register
case class R15(value: Long)                extends Register
case class Rip(value: Long)                extends Register
case class Eflags(value: Long)             extends Register
case class Cs(value: Long)                 extends Register
case class Ss(value: Long)                 extends Register
case class Ds(value: Long)                 extends Register
case class Es(value: Long)                 extends Register
case class Fs(value: Long)                 extends Register
case class Gs(value: Long)                 extends Register
case class St0(value: Long)                extends Register
case class St1(value: Long)                extends Register
case class St2(value: Long)                extends Register
case class St3(value: Long)                extends Register
case class St4(value: Long)                extends Register
case class St5(value: Long)                extends Register
case class St6(value: Long)                extends Register
case class St7(value: Long)                extends Register
case class Fctrl(value: Long)              extends Register
case class Ftag(value: Long)               extends Register
case class Fiseg(value: Long)              extends Register
case class Fioff(value: Long)              extends Register
case class Foseg(value: Long)              extends Register
case class Fooff(value: Long)              extends Register
case class Fop(value: Long)                extends Register
case class FsBase(value: Long)             extends Register
case class GsBase(value: Long)             extends Register
case class KgsBase(value: Long)            extends Register
case class Cr0(value: Long)                extends Register
case class Cr2(value: Long)                extends Register
case class Cr3(value: Long)                extends Register
case class Cr4(value: Long)                extends Register
case class Cr8(value: Long)                extends Register
case class Efer(value: Long)               extends Register
case class Mxcsr(value: Long)              extends Register
case class Other(number: Int, value: Long) extends Register

object Register {
    implicit val reads: Reads[Register] = Reads { js =>
        js match {
            case o: JsObject if o.keys.size == 1 =>
                o.fields.head match {
                    case ("Rax", JsNumber(v))     => JsSuccess(Rax(v.toLong))
                    case ("Rbx", JsNumber(v))     => JsSuccess(Rbx(v.toLong))
                    case ("Rcx", JsNumber(v))     => JsSuccess(Rcx(v.toLong))
                    case ("Rdx", JsNumber(v))     => JsSuccess(Rdx(v.toLong))
                    case ("Rsi", JsNumber(v))     => JsSuccess(Rsi(v.toLong))
                    case ("Rdi", JsNumber(v))     => JsSuccess(Rdi(v.toLong))
                    case ("Rbp", JsNumber(v))     => JsSuccess(Rbp(v.toLong))
                    case ("Rsp", JsNumber(v))     => JsSuccess(Rsp(v.toLong))
                    case ("R8", JsNumber(v))      => JsSuccess(R8(v.toLong))
                    case ("R9", JsNumber(v))      => JsSuccess(R9(v.toLong))
                    case ("R10", JsNumber(v))     => JsSuccess(R10(v.toLong))
                    case ("R11", JsNumber(v))     => JsSuccess(R11(v.toLong))
                    case ("R12", JsNumber(v))     => JsSuccess(R12(v.toLong))
                    case ("R13", JsNumber(v))     => JsSuccess(R13(v.toLong))
                    case ("R14", JsNumber(v))     => JsSuccess(R14(v.toLong))
                    case ("R15", JsNumber(v))     => JsSuccess(R15(v.toLong))
                    case ("Rip", JsNumber(v))     => JsSuccess(Rip(v.toLong))
                    case ("Eflags", JsNumber(v))  => JsSuccess(Eflags(v.toLong))
                    case ("Cs", JsNumber(v))      => JsSuccess(Cs(v.toLong))
                    case ("Ss", JsNumber(v))      => JsSuccess(Ss(v.toLong))
                    case ("Ds", JsNumber(v))      => JsSuccess(Ds(v.toLong))
                    case ("Es", JsNumber(v))      => JsSuccess(Es(v.toLong))
                    case ("Fs", JsNumber(v))      => JsSuccess(Fs(v.toLong))
                    case ("Gs", JsNumber(v))      => JsSuccess(Gs(v.toLong))
                    case ("St0", JsNumber(v))     => JsSuccess(St0(v.toLong))
                    case ("St1", JsNumber(v))     => JsSuccess(St1(v.toLong))
                    case ("St2", JsNumber(v))     => JsSuccess(St2(v.toLong))
                    case ("St3", JsNumber(v))     => JsSuccess(St3(v.toLong))
                    case ("St4", JsNumber(v))     => JsSuccess(St4(v.toLong))
                    case ("St5", JsNumber(v))     => JsSuccess(St5(v.toLong))
                    case ("St6", JsNumber(v))     => JsSuccess(St6(v.toLong))
                    case ("St7", JsNumber(v))     => JsSuccess(St7(v.toLong))
                    case ("Fctrl", JsNumber(v))   => JsSuccess(Fctrl(v.toLong))
                    case ("Ftag", JsNumber(v))    => JsSuccess(Ftag(v.toLong))
                    case ("Fiseg", JsNumber(v))   => JsSuccess(Fiseg(v.toLong))
                    case ("Fioff", JsNumber(v))   => JsSuccess(Fioff(v.toLong))
                    case ("Foseg", JsNumber(v))   => JsSuccess(Foseg(v.toLong))
                    case ("Fooff", JsNumber(v))   => JsSuccess(Fooff(v.toLong))
                    case ("Fop", JsNumber(v))     => JsSuccess(Fop(v.toLong))
                    case ("FsBase", JsNumber(v))  => JsSuccess(FsBase(v.toLong))
                    case ("GsBase", JsNumber(v))  => JsSuccess(GsBase(v.toLong))
                    case ("KgsBase", JsNumber(v)) => JsSuccess(KgsBase(v.toLong))
                    case ("Cr0", JsNumber(v))     => JsSuccess(Cr0(v.toLong))
                    case ("Cr2", JsNumber(v))     => JsSuccess(Cr2(v.toLong))
                    case ("Cr3", JsNumber(v))     => JsSuccess(Cr3(v.toLong))
                    case ("Cr4", JsNumber(v))     => JsSuccess(Cr4(v.toLong))
                    case ("Cr8", JsNumber(v))     => JsSuccess(Cr8(v.toLong))
                    case ("Efer", JsNumber(v))    => JsSuccess(Efer(v.toLong))
                    case ("Mxcsr", JsNumber(v))   => JsSuccess(Mxcsr(v.toLong))
                    case ("Other", JsObject(fields)) =>
                        (fields.get("number"), fields.get("value")) match {
                            case (Some(JsNumber(number)), Some(JsNumber(value))) =>
                                JsSuccess(Other(number.toInt, value.toLong))
                            case _ =>
                                JsError("Invalid fields for Other register")
                        }
                    case _ => JsError("Unknown register")
                }
            case _ => JsError("Register must be an object with one field")
        }
    }
}

// Frame
case class Frame(
    level: Int,
    address: Long,
    function: Option[String],
    file: Option[String],
    line: Option[Int]
)
object Frame {
    implicit val reads: Reads[Frame] = Json.reads[Frame]
}

// Variable
case class Variable(
    name: String,
    var_type: String,
    value: Option[String],
    is_arg: Boolean
)
object Variable {
    implicit val reads: Reads[Variable] = Json.reads[Variable]
}

// DebugInfo
case class DebugInfo(
    regs: Seq[Register],
    frame: Frame,
    variables: Seq[Variable]
)
object DebugInfo {
    implicit val reads: Reads[DebugInfo] = Json.reads[DebugInfo]
}
