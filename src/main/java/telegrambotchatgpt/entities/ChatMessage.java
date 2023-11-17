package telegrambotchatgpt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "message_time", nullable = false)
    private LocalDateTime messageTime;

    @Column(name = "message_author", nullable = false)
    private String messageAuthor;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
