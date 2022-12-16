package back.kickoff.kickoffback.services;

import back.kickoff.kickoffback.model.Announcement;
import back.kickoff.kickoffback.model.CourtOwner;
import back.kickoff.kickoffback.repositories.AnnouncementRepository;
import back.kickoff.kickoffback.repositories.CourtOwnerRepository;
import com.google.gson.Gson;
import jakarta.persistence.ManyToOne;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AnnouncementService {

    private final CourtOwnerRepository courtOwnerRepository;
    private final AnnouncementRepository announcementRepository;

    static class AnnouncmentFrontend{
        Long id;
        Long courtOwnerId;
        String title ;
        String body ;
        String cni ; // Attachments
        String date ;

        public AnnouncmentFrontend(Long id, Long courtOwnerId, String title, String body, String cni, String date) {
            this.id = id;
            this.courtOwnerId = courtOwnerId;
            this.title = title;
            this.body = body;
            this.cni = cni;
            this.date = date;
        }
    }

    public AnnouncementService(CourtOwnerRepository courtOwnerRepository, AnnouncementRepository announcementRepository, AnnouncementRepository announcementRepository1) {
        this.courtOwnerRepository = courtOwnerRepository;
        this.announcementRepository = announcementRepository1;
    }


    public String addAnnouncement(String information) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(information);
        Long courtOwnerId = jsonObject.getLong("courtOwnerId");
        String title = jsonObject.getString("title");
        String body = jsonObject.getString("body");
        String attachmentsURL = jsonObject.getString("attachments");
        String dateString = jsonObject.getString("date");

        Optional<CourtOwner> courtOwnerOptional = courtOwnerRepository.findById(courtOwnerId) ;
        if(courtOwnerOptional.isEmpty()){
            return "CourtOwner do not exist" ;
        }
        CourtOwner courtOwner= courtOwnerOptional.get() ;
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setTitle(title);
        newAnnouncement.setBody(body);
        newAnnouncement.setCni(attachmentsURL);
        Date date;
        try
        {
            SimpleDateFormat obj = new SimpleDateFormat("MM/dd/yyyy");
            long date2 = obj.parse(dateString).getTime();
            date = new Date(date2);
        }
        catch (Exception e)
        {
            return "In valid date2";
        }
        newAnnouncement.setDate(date);
        newAnnouncement.setCourtOwner(courtOwner);
        courtOwner.getAnnouncements().add(newAnnouncement) ;

        announcementRepository.save(newAnnouncement) ;
        courtOwnerRepository.save(courtOwner) ;

        return new Gson().toJson("Success");
    }


    public String deleteAnnouncement(Long courtOwnerId, String information) throws JSONException {
        JSONObject jsonObject = new JSONObject(information);
        Long id = jsonObject.getLong("id");

        Optional<CourtOwner> courtOwnerOptional = courtOwnerRepository.findById(courtOwnerId) ;
        if(courtOwnerOptional.isEmpty()){
            return "CourtOwner do not exist" ;
        }
        CourtOwner courtOwner= courtOwnerOptional.get() ;

        Optional<Announcement> announcementOptional = announcementRepository.findById(id) ;
        if(announcementOptional.isEmpty()){
            return "Announcement do not exist" ;
        }
        Announcement announcement= announcementOptional.get() ;

        if(!courtOwner.getId().equals(announcement.getCourtOwner().getId())){
            return "Announcement do not belong to that CourtOwner" ;
        }
        courtOwner.getAnnouncements().remove(announcement) ;
        announcementRepository.deleteById(announcement.getId());
        courtOwnerRepository.save(courtOwner) ;
        return "Success" ;
    }

    public String getAnnouncement(Long courtOwnerId) {
        Optional<CourtOwner> courtOwnerOptional = courtOwnerRepository.findById(courtOwnerId) ;
        if(courtOwnerOptional.isEmpty()){
            return "CourtOwner do not exist" ;
        }
        CourtOwner courtOwner = courtOwnerOptional.get() ;
        List<Announcement> announcements= courtOwner.getAnnouncements() ;
        List<AnnouncmentFrontend> announcmentFrontends = new ArrayList<AnnouncmentFrontend>(announcements.size()) ;
        for (Announcement a: announcements){
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strDate = dateFormat.format(a.getDate());
            announcmentFrontends.add(new AnnouncmentFrontend(a.getId(), a.getCourtOwner().getId(), a.getTitle(), a.getBody(), a.getCni(), strDate )) ;
        }
        return new Gson().toJson(announcmentFrontends);
    }
}
