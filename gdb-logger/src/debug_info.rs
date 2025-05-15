use serde::Serialize;

#[derive(Debug, Serialize)]
pub enum Register {
    Rax(u64), // 0
    Rbx(u64),
    Rcx(u64),
    Rdx(u64),
    Rsi(u64),
    Rdi(u64),
    Rbp(u64),
    Rsp(u64),
    R8(u64),
    R9(u64),
    R10(u64),
    R11(u64),
    R12(u64),
    R13(u64),
    R14(u64),
    R15(u64),
    Rip(u64),
    Eflags(u64),
    Cs(u64),
    Ss(u64),
    Ds(u64),
    Es(u64),
    Fs(u64),
    Gs(u64),
    St0(u64),
    St1(u64),
    St2(u64),
    St3(u64),
    St4(u64),
    St5(u64),
    St6(u64),
    St7(u64),
    Fctrl(u64),
    Ftag(u64),
    Fiseg(u64),
    Fioff(u64),
    Foseg(u64),
    Fooff(u64),
    Fop(u64),    // 39
    FsBase(u64), // 155
    GsBase(u64),
    KgsBase(u64),
    Cr0(u64),
    Cr2(u64),
    Cr3(u64),
    Cr4(u64),
    Cr8(u64),
    Efer(u64),
    Mxcsr(u64),
    Other { number: u8, value: u64 },
}

impl From<(u8, u64)> for Register {
    fn from(value: (u8, u64)) -> Self {
        let (number, value) = value;

        match number {
            0 => Register::Rax(value),
            1 => Register::Rbx(value),
            2 => Register::Rcx(value),
            3 => Register::Rdx(value),
            4 => Register::Rsi(value),
            5 => Register::Rdi(value),
            6 => Register::Rbp(value),
            7 => Register::Rsp(value),
            8 => Register::R8(value),
            9 => Register::R9(value),
            10 => Register::R10(value),
            11 => Register::R11(value),
            12 => Register::R12(value),
            13 => Register::R13(value),
            14 => Register::R14(value),
            15 => Register::R15(value),
            16 => Register::Rip(value),
            17 => Register::Eflags(value),
            18 => Register::Cs(value),
            19 => Register::Ss(value),
            20 => Register::Ds(value),
            21 => Register::Es(value),
            22 => Register::Fs(value),
            23 => Register::Gs(value),
            24 => Register::St0(value),
            25 => Register::St1(value),
            26 => Register::St2(value),
            27 => Register::St3(value),
            28 => Register::St4(value),
            29 => Register::St5(value),
            30 => Register::St6(value),
            31 => Register::St7(value),
            32 => Register::Fctrl(value),
            33 => Register::Ftag(value),
            34 => Register::Fiseg(value),
            35 => Register::Fioff(value),
            36 => Register::Foseg(value),
            37 => Register::Fooff(value),
            39 => Register::Fop(value),
            155 => Register::FsBase(value),
            156 => Register::GsBase(value),
            157 => Register::KgsBase(value),
            158 => Register::Cr0(value),
            159 => Register::Cr2(value),
            160 => Register::Cr3(value),
            161 => Register::Cr4(value),
            162 => Register::Cr8(value),
            163 => Register::Efer(value),
            180 => Register::Mxcsr(value),
            _ => Register::Other { number, value },
        }
    }
}

impl Register {
    pub fn is_other(&self) -> bool {
        matches!(self, Register::Other { .. })
    }
}

#[derive(Debug, Serialize)]
pub struct Frame {
    pub level: u32,
    pub address: u64,
    pub function: Option<String>,
    pub file: Option<String>,
    pub line: Option<u32>,
}

impl From<gdbmi::frame::Frame> for Frame {
    fn from(value: gdbmi::frame::Frame) -> Self {
        Self {
            level: value.level,
            address: value.address.0,
            function: value.function,
            file: value.file.map(|f| f.to_string()),
            line: value.line,
        }
    }
}

#[derive(Debug, Serialize)]
pub struct Variable {
    pub name: String,
    pub var_type: String,
    pub value: Option<String>,
    pub is_arg: bool,
}

impl From<gdbmi::variable::Variable> for Variable {
    fn from(value: gdbmi::variable::Variable) -> Self {
        Self {
            name: value.name,
            var_type: value.var_type,
            value: value.value.map(|v| v.to_string()),
            is_arg: value.is_arg,
        }
    }
}

#[derive(Debug, Serialize)]
pub struct DebugInfo {
    pub regs: Vec<Register>,
    pub frame: Frame,
    pub variables: Vec<Variable>,
}
