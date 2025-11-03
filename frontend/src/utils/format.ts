
export const moneyBR = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

export function toBRL(n: number) {
  return moneyBR.format(n ?? 0)
}
