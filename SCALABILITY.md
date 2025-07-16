
| Recurso            | Como ajuda na escalabilidade              |
|--------------------|-------------------------------------------|
| Redis Cache        | Reduz latência e load em banco            |
| Fila (SQS/Kafka)   | Absorve picos e desacopla processos       |
| Services isoladas  | Permitem escalabilidade horizontal        |
| Spring Security    | Protege sem prejudicar performance        |
| Modularidade       | Permite split em microsserviços           |
| Logging/exceptions | Facilitam observabilidade e monitoramento |

futuramente eu criaria uma policy de auto scaling de cpu e de memória de acordo com status dos monitores de consumo
desses recurso, criando um target time de 75% de consumo dos mesmos para realizar a escala.