export function capitalizarPalavras(texto: string): string {
  return texto
    .trim()
    .split(/\s+/)
    .filter(p => p.length > 0)
    .map(p => p.charAt(0).toUpperCase() + p.slice(1).toLowerCase())
    .join(' ');
}
