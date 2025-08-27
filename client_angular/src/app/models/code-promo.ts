export interface CodePromo {
  id?: number;
  code: string;
  type: 'PERCENTAGE' | 'FIXED';
  amount: number;
  maxDiscount?: number;
  minOrderTotal?: number;
  startsAt?: string; // ISO string
  endsAt?: string;   // ISO string
  active: boolean;
  globalUsageLimit?: number;
  usedCount: number;
  perUserLimit?: number;
}
export interface ApplyPromoRequest {
  panierId: number;
  utilisateurId: number;
  code: string;
}

export interface ApplyPromoResponse {
  panier: any;           // Ici tu peux utiliser ton modèle Panier si tu l'as défini
  appliedCode: string;
  discountAmount: number;
  newTotal: number;
}
